package sopt.makers.authentication.application.port.out.user;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.User;

public interface UserActivityHistoryRepository {
  void save(User user, Activity activity);

  void update(User user, ActivityList activityList);
}
