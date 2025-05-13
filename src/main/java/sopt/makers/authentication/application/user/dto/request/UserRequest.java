package sopt.makers.authentication.application.user.dto.request;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.usecase.user.port.in.UpdateUserProfileUsecase.SoptActivityCommand;
import sopt.makers.authentication.usecase.user.port.in.UpdateUserProfileUsecase.UserProfileCommand;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public class UserRequest {
  public record UserProfileInfo(
      String profileImage,
      LocalDate birthday,
      @NotNull(message = "핸드폰 번호는 필수 입력 값입니다.") String phone,
      @NotNull(message = "이메일은 필수 입력 값입니다.")
          @Pattern(
              regexp =
                  "^[0-9a-zA-Z]([-_￦.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_￦.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")
          String email,
      List<SoptActivityInfo> soptActivities) {
    public UserProfileCommand toCommand() {
      List<SoptActivityCommand> activityCommandList =
          soptActivities.stream().map(SoptActivityInfo::toCommand).toList();
      return UserProfileCommand.of(profileImage, birthday, phone, email, activityCommandList);
    }
  }

  public record SoptActivityInfo(Long activityId, int generation, String part, String team) {
    public SoptActivityCommand toCommand() {
      return SoptActivityCommand.of(activityId, generation, part, team);
    }
  }
}
