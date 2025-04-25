package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;

import java.util.List;

public interface GetUserInformationUsecase {

  UserProfileAndActivityInfo getUserInformation(long userId);

  record UserProfileAndActivityInfo(
      String name,
      String profileImage,
      String birthday,
      String phone,
      String email,
      List<UserActivityinfo> soptActivities) {

    public static UserProfileAndActivityInfo of(User user, ActivityList activities) {
      Profile profile = user.getProfile();
      List<UserActivityinfo> userActivityinfos =
          activities.getActivities().stream().map(UserActivityinfo::of).toList();

      return new UserProfileAndActivityInfo(
          profile.name(), profile.profileImage().orElse(null),
          profile.birthday().toString(), profile.phone(),
          profile.email().orElse(null), userActivityinfos);
    }
  }

  record UserActivityinfo(long activityId, int generation, String part, String team) {
    public static UserActivityinfo of(Activity activity) {
      return new UserActivityinfo(
          activity.getId(),
          activity.getGeneration(),
          activity.getPart().getName(),
          activity.getTeam() != null ? activity.getTeam().getName() : null);
    }
  }
}
