package sopt.makers.authentication.application.user.dto.response;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.usecase.user.port.in.GetUserProfileUsecase;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class UserResponse {
  public record UserProfileAndActivity(
      Long userId,
      String name,
      String profileImage,
      String birthday,
      String phone,
      String email,
      Integer latestGeneration,
      List<UserActivityDetail> soptActivities) {
    public static UserProfileAndActivity from(
        GetUserProfileUsecase.UserProfileAndActivityInfo userProfileAndActivityInfo) {
      List<UserActivityDetail> soptActivities =
          userProfileAndActivityInfo.soptActivities().stream()
              .map(UserActivityDetail::from)
              .toList();

      return new UserProfileAndActivity(
          userProfileAndActivityInfo.userId(),
          userProfileAndActivityInfo.name(),
          userProfileAndActivityInfo.profileImage(),
          userProfileAndActivityInfo.birthday(),
          userProfileAndActivityInfo.phone(),
          userProfileAndActivityInfo.email(),
          userProfileAndActivityInfo.latestGeneration(),
          soptActivities);
    }

    public static List<UserProfileAndActivity> from(
        List<GetUserProfileUsecase.UserProfileAndActivityInfo> infoList) {
      return infoList.stream().map(UserResponse.UserProfileAndActivity::from).toList();
    }
  }

  public record UserActivityDetail(long activityId, int generation, String part, String team) {
    public static UserActivityDetail from(GetUserProfileUsecase.UserActivityinfo userActivityInfo) {
      return new UserActivityDetail(
          userActivityInfo.activityId(),
          userActivityInfo.generation(),
          userActivityInfo.part(),
          userActivityInfo.team());
    }
  }
}
