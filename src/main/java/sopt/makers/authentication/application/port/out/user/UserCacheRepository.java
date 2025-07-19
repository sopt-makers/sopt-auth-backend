package sopt.makers.authentication.application.port.out.user;

import sopt.makers.authentication.adapter.out.cache.dto.CachedUserProfile;

import java.util.List;
import java.util.Map;

public interface UserCacheRepository {
  Map<Long, CachedUserProfile> getAllPresent(List<Long> userIds);

  void put(Long userId, CachedUserProfile profile);

  void evict(Long userId);

  void evictAll();
}
