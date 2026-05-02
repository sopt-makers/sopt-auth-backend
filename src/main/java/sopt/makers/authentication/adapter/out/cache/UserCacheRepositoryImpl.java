package sopt.makers.authentication.adapter.out.cache;

import static sopt.makers.authentication.common.constant.SystemConstant.USER_CACHE_NAME;

import sopt.makers.authentication.adapter.out.cache.dto.CachedUserProfile;
import sopt.makers.authentication.adapter.out.cache.mapper.UserMapper;
import sopt.makers.authentication.application.port.out.user.UserCacheRepository;
import sopt.makers.authentication.domain.user.User;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.redisson.api.BatchResult;
import org.redisson.api.RBatch;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.Codec;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserCacheRepositoryImpl implements UserCacheRepository {
  private static final Duration TTL = Duration.ofDays(1);
  private static final String KEY_PREFIX = USER_CACHE_NAME + ":";

  private final RedissonClient redissonClient;
  private final UserMapper userMapper;
  private final Codec codec;

  public UserCacheRepositoryImpl(
      RedissonClient redissonClient, UserMapper userMapper, ObjectMapper objectMapper) {
    this.redissonClient = redissonClient;
    this.userMapper = userMapper;
    this.codec = new TypedJsonJacksonCodec(CachedUserProfile.class, objectMapper);
  }

  @Override
  public Map<Long, User> getAllPresent(List<Long> userIds) {
    if (userIds.isEmpty()) return Map.of();

    RBatch batch = redissonClient.createBatch();
    userIds.forEach(id -> batch.getBucket(KEY_PREFIX + id, codec).getAsync());
    BatchResult<?> result = batch.execute();

    List<?> responses = result.getResponses();
    Map<Long, User> users = new HashMap<>();
    for (int i = 0; i < userIds.size(); i++) {
      CachedUserProfile profile = (CachedUserProfile) responses.get(i);
      if (profile != null) {
        users.put(userIds.get(i), userMapper.toDomain(profile));
      }
    }
    return users;
  }

  @Override
  public void put(User user) {
    redissonClient.getBucket(KEY_PREFIX + user.getId(), codec).set(userMapper.toCache(user), TTL);
  }
}
