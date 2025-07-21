package sopt.makers.authentication.application.port.out.user;

import sopt.makers.authentication.domain.user.User;

import java.util.List;
import java.util.Map;

public interface UserCacheRepository {
  Map<Long, User> getAllPresent(List<Long> userIds);

  void put(Long userId, User user);

  void evict(Long userId);

  void evictAll();
}
