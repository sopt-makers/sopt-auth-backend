package sopt.makers.authentication.adapter.in.web.dto.user.response;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.application.port.in.user.GetUserProfileUsecase;

import java.time.LocalDate;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class UserResponse {
  public record UserProfileAndActivity(
      Long userId,
      String name,
      String profileImage,
      LocalDate birthday,
      String phone,
      String email,
      Integer lastGeneration,
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
          userProfileAndActivityInfo.lastGeneration(),
          soptActivities);
    }

    public static List<UserProfileAndActivity> from(
        List<GetUserProfileUsecase.UserProfileAndActivityInfo> infoList) {
      return infoList.stream().map(UserProfileAndActivity::from).toList();
    }
  }

  public record UserActivityDetail(
      long activityId, int generation, String part, String team, String role, boolean isSopt) {
    public static UserActivityDetail from(GetUserProfileUsecase.UserActivityInfo userActivityInfo) {
      return new UserActivityDetail(
          userActivityInfo.activityId(),
          userActivityInfo.generation(),
          userActivityInfo.part(),
          userActivityInfo.team(),
          userActivityInfo.role(),
          userActivityInfo.isSopt());
    }
  }

  public record UserCountByGeneration(int numberOfMembersAtGeneration) {
    public static UserCountByGeneration from(
        GetUserProfileUsecase.UserCountByGeneration userCountByGeneration) {
      return new UserCountByGeneration(userCountByGeneration.count());
    }
  }

  public record PaginatedUserProfiles(
      List<UserProfileAndActivity> profiles, boolean hasNext, long totalCount) {
    public static PaginatedUserProfiles from(
        GetUserProfileUsecase.PaginatedUserProfiles paginatedData) {
      List<UserProfileAndActivity> profiles =
          paginatedData.profiles().stream().map(UserProfileAndActivity::from).toList();
      return new PaginatedUserProfiles(
          profiles, paginatedData.hasNext(), paginatedData.totalCount());
    }
  }
}
