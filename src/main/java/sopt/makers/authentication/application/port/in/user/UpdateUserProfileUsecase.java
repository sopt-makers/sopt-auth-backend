package sopt.makers.authentication.application.port.in.user;

import java.time.LocalDate;
import java.util.List;

public interface UpdateUserProfileUsecase {
  void updateUserProfile(UserProfileCommand userProfileCommand);

  record UserProfileCommand(
      Long userId,
      String profileImage,
      LocalDate birthday,
      String phone,
      String email,
      List<SoptActivityCommand> soptActivities) {
    public static UserProfileCommand of(
        Long userId,
        String profileImage,
        LocalDate birthday,
        String phone,
        String email,
        List<SoptActivityCommand> soptActivities) {
      return new UserProfileCommand(userId, profileImage, birthday, phone, email, soptActivities);
    }
  }

  record SoptActivityCommand(Long activityId, String team) {
    public static SoptActivityCommand of(Long activityId, String team) {
      return new SoptActivityCommand(activityId, team);
    }
  }
}
