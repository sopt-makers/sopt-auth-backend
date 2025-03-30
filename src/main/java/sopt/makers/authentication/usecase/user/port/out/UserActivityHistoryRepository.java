package sopt.makers.authentication.usecase.user.port.out;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.User;

public interface UserActivityHistoryRepository {
  void save(User user, Activity activity);
}
