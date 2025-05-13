package sopt.makers.authentication.usecase.user.port.in;

import java.time.LocalDate;
import java.util.List;

public interface UpdateUserProfileUsecase {
  void updateUserProfile(UserProfileCommand userProfileCommand);

  record UserProfileCommand(
      String profileImage,
      LocalDate birthday,
      String phone,
      String email,
      List<SoptActivityCommand> soptActivities) {
    public static UserProfileCommand of(
        String profileImage,
        LocalDate birthday,
        String phone,
        String email,
        List<SoptActivityCommand> soptActivities) {
      return new UserProfileCommand(profileImage, birthday, phone, email, soptActivities);
    }
  }

  record SoptActivityCommand(Long activityId, int generation, String part, String team) {
    public static SoptActivityCommand of(
        Long activityId, int generation, String part, String team) {
      return new SoptActivityCommand(activityId, generation, part, team);
    }
  }
}
