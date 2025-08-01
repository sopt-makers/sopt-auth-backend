package sopt.makers.authentication.application.port.in.user;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserOrderBy;

import java.time.LocalDate;
import java.util.List;

public interface GetUserProfileUsecase {

  List<UserProfileAndActivityInfo> getUserInformation(List<Long> userIds);

  PaginatedUserProfiles getUserInformationByFilters(
      Integer generation,
      Part part,
      String name,
      Team team,
      int offset,
      int limit,
      UserOrderBy orderBy);

  UserCountByGeneration getUserCountByGeneration(int generation);

  record UserProfileAndActivityInfo(
      Long userId,
      String name,
      String profileImage,
      LocalDate birthday,
      String phone,
      String email,
      boolean hasProfile,
      Integer lastGeneration,
      List<UserActivityInfo> soptActivities) {

    public static UserProfileAndActivityInfo of(User user, ActivityList activities) {
      Profile profile = user.getProfile();
      List<UserActivityInfo> userActivityInfos =
          activities.getActivities().stream().map(UserActivityInfo::of).toList();

      return new UserProfileAndActivityInfo(
          user.getId(),
          profile.name(),
          profile.profileImage().orElse(null),
          profile.birthday(),
          profile.phone(),
          profile.email().orElse(null),
          profile.hasProfile(),
          activities.getLastActivity().getGeneration(),
          userActivityInfos);
    }
  }

  record UserActivityInfo(long activityId, int generation, String part, String team) {
    public static UserActivityInfo of(Activity activity) {
      return new UserActivityInfo(
          activity.getId(),
          activity.getGeneration(),
          activity.getPart().getName(),
          activity.getTeam() != null ? activity.getTeam().getName() : null);
    }
  }

  record UserCountByGeneration(int count) {}

  record PaginatedUserProfiles(
      List<UserProfileAndActivityInfo> profiles, boolean hasNext, long totalCount) {}
}
