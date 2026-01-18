package sopt.makers.authentication.adapter.out.cache;

import static sopt.makers.authentication.common.constant.SystemConstant.USER_CACHE_NAME;

import sopt.makers.authentication.adapter.out.cache.dto.CachedUserProfile;
import sopt.makers.authentication.adapter.out.cache.mapper.UserMapper;
import sopt.makers.authentication.application.port.out.user.UserCacheRepository;
import sopt.makers.authentication.domain.user.User;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.redisson.api.RMapCache;
import org.redisson.api.RedissonClient;
import org.redisson.codec.TypedJsonJacksonCodec;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

@Component
public class UserCacheRepositoryImpl implements UserCacheRepository {
  private static final long TTL_DAYS = 1;
  private final RMapCache<Long, CachedUserProfile> cache;
  private final UserMapper userMapper;

  public UserCacheRepositoryImpl(
      RedissonClient redissonClient, UserMapper userMapper, ObjectMapper objectMapper) {
    this.cache =
        redissonClient.getMapCache(
            USER_CACHE_NAME,
            new TypedJsonJacksonCodec(Long.class, CachedUserProfile.class, objectMapper));
    this.userMapper = userMapper;
  }

  @Override
  public Map<Long, User> getAllPresent(List<Long> userIds) {
    if (userIds.isEmpty()) return Map.of();

    Map<Long, CachedUserProfile> cachedProfiles = cache.getAll(new HashSet<>(userIds));

    return cachedProfiles.entrySet().stream()
        .collect(Collectors.toMap(Map.Entry::getKey, e -> userMapper.toDomain(e.getValue())));
  }

  @Override
  public void put(User user) {
    cache.put(user.getId(), userMapper.toCache(user), TTL_DAYS, TimeUnit.DAYS);
  }
}
