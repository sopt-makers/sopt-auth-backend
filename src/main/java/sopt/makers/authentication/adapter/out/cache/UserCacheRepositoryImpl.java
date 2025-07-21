package sopt.makers.authentication.adapter.out.cache;

import static sopt.makers.authentication.adapter.out.common.exception.CacheFailure.CACHE_NOT_CONFIGURED;
import static sopt.makers.authentication.common.constant.SystemConstant.USER_CACHE_NAME;

import sopt.makers.authentication.adapter.out.cache.dto.CachedUserProfile;
import sopt.makers.authentication.adapter.out.cache.mapper.UserMapper;
import sopt.makers.authentication.adapter.out.common.exception.CacheException;
import sopt.makers.authentication.application.port.out.user.UserCacheRepository;
import sopt.makers.authentication.domain.user.User;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.stereotype.Component;

import com.github.benmanes.caffeine.cache.Cache;

@Component
public class UserCacheRepositoryImpl implements UserCacheRepository {
  private final Cache<Object, Object> cache;
  private final UserMapper userMapper;

  public UserCacheRepositoryImpl(CacheManager cacheManager, UserMapper userMapper) {
    CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(USER_CACHE_NAME);
    boolean notExistCache = caffeineCache == null;

    if (notExistCache) {
      throw new CacheException(CACHE_NOT_CONFIGURED);
    }
    this.cache = caffeineCache.getNativeCache();
    this.userMapper = userMapper;
  }

  @Override
  public Map<Long, User> getAllPresent(List<Long> userIds) {
    Map<Object, Object> raw = cache.getAllPresent(userIds);
    return raw.entrySet().stream()
        .collect(
            Collectors.toMap(
                e -> (Long) e.getKey(),
                e -> userMapper.toDomain((CachedUserProfile) e.getValue())));
  }

  @Override
  public void put(Long userId, User user) {
    CachedUserProfile cached = userMapper.toCache(user);
    cache.put(userId, cached);
  }

  @Override
  public void evict(Long userId) {
    cache.invalidate(userId);
  }

  @Override
  public void evictAll() {
    cache.invalidateAll();
  }
}
