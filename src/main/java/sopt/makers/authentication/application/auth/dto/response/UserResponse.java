package sopt.makers.authentication.application.auth.dto.response;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.usecase.auth.port.in.GetUserInformationUsecase;

import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class UserResponse {
  public record UserProfileAndActivity(
      String name,
      String profileImage,
      String birthday,
      String phone,
      String email,
      List<UserActivityDetail> soptActivities) {
    public static UserProfileAndActivity from(
        GetUserInformationUsecase.UserProfileAndActivityInfo userProfileAndActivityInfo) {
      List<UserActivityDetail> soptActivities =
          userProfileAndActivityInfo.soptActivities().stream()
              .map(UserActivityDetail::from)
              .toList();

      return new UserProfileAndActivity(
          userProfileAndActivityInfo.name(),
          userProfileAndActivityInfo.profileImage(),
          userProfileAndActivityInfo.birthday(),
          userProfileAndActivityInfo.phone(),
          userProfileAndActivityInfo.email(),
          soptActivities);
    }
  }

  public record UserActivityDetail(long activityId, int generation, String part, String team) {
    public static UserActivityDetail from(
        GetUserInformationUsecase.UserActivityinfo userActivityInfo) {
      return new UserActivityDetail(
          userActivityInfo.activityId(),
          userActivityInfo.generation(),
          userActivityInfo.part(),
          userActivityInfo.team());
    }
  }
}
