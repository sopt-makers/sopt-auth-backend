package sopt.makers.authentication.application.user.api;

import static sopt.makers.authentication.application.user.dto.response.UserResponse.*;
import static sopt.makers.authentication.usecase.user.port.in.GetSocialAccountPlatform.*;

import sopt.makers.authentication.support.code.domain.success.UserSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.usecase.user.port.in.GetSocialAccountPlatform;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserApiController implements UserApi {
  private final GetSocialAccountPlatform getSocialAccountPlatformUsecase;

  @Override
  @GetMapping("/social")
  public ResponseEntity<BaseResponse<?>> getRegisterSocialAccountPlatform(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "phone") String phone) {
    GetSocialAccountPlatformCommand command = new GetSocialAccountPlatformCommand(name, phone);
    SocialAccountPlatformInfo socialAccountPlatform =
        getSocialAccountPlatformUsecase.getSocialAccountPlatform(command);

    return ResponseEntity.status(UserSuccess.GET_SOCIAL_ACCOUNT_PLATFORM.getStatus().value())
        .body(
            BaseResponse.ofSuccess(
                UserSuccess.GET_SOCIAL_ACCOUNT_PLATFORM,
                SocialAccountPlatform.from(socialAccountPlatform)));
  }
}
