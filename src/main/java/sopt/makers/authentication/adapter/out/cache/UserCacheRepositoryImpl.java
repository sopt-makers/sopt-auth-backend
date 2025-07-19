package sopt.makers.authentication.adapter.out.cache;

import static sopt.makers.authentication.adapter.out.common.exception.CacheFailure.CACHE_NOT_CONFIGURED;
import static sopt.makers.authentication.common.constant.SystemConstant.USER_CACHE_NAME;

import sopt.makers.authentication.adapter.out.cache.dto.CachedUserProfile;
import sopt.makers.authentication.adapter.out.common.exception.CacheException;
import sopt.makers.authentication.application.port.out.user.UserCacheRepository;

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

  public UserCacheRepositoryImpl(CacheManager cacheManager) {
    CaffeineCache caffeineCache = (CaffeineCache) cacheManager.getCache(USER_CACHE_NAME);
    boolean notExistCache = caffeineCache == null;

    if (notExistCache) {
      throw new CacheException(CACHE_NOT_CONFIGURED);
    }
    this.cache = caffeineCache.getNativeCache();
  }

  @Override
  public Map<Long, CachedUserProfile> getAllPresent(List<Long> userIds) {
    Map<Object, Object> raw = cache.getAllPresent(userIds);
    return raw.entrySet().stream()
        .collect(Collectors.toMap(e -> (Long) e.getKey(), e -> (CachedUserProfile) e.getValue()));
  }

  @Override
  public void put(Long userId, CachedUserProfile profile) {
    cache.put(userId, profile);
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
