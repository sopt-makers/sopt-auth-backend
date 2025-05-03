package sopt.makers.authentication.usecase.user.port.out;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.User;

import java.util.List;
import java.util.Map;

public interface UserActivityHistoryRepository {
  void save(User user, Activity activity);

  ActivityList findByUser(Long userId);

  Map<Long, ActivityList> findAllByUserIdIn(List<Long> userId);
}
