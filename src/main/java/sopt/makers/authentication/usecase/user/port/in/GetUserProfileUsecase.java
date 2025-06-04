package sopt.makers.authentication.usecase.user.port.in;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;

import java.time.LocalDate;
import java.util.List;

public interface GetUserProfileUsecase {

  List<UserProfileAndActivityInfo> getUserInformation(List<Long> userIds);

  record UserProfileAndActivityInfo(
      Long userId,
      String name,
      String profileImage,
      LocalDate birthday,
      String phone,
      String email,
      Integer lastGeneration,
      List<UserActivityinfo> soptActivities) {

    public static UserProfileAndActivityInfo of(User user, ActivityList activities) {
      Profile profile = user.getProfile();
      List<UserActivityinfo> userActivityinfos =
          activities.getActivities().stream().map(UserActivityinfo::of).toList();

      return new UserProfileAndActivityInfo(
          user.getId(),
          profile.name(),
          profile.profileImage().orElse(null),
          profile.birthday(),
          profile.phone(),
          profile.email().orElse(null),
          activities.getLastActivity().getGeneration(),
          userActivityinfos);
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
