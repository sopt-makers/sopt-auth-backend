package sopt.makers.authentication.adapter.in.web.controller.auth;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.adapter.in.web.common.code.SocialAccountSuccess;
import sopt.makers.authentication.adapter.in.web.dto.auth.request.SocialAccountRequest;
import sopt.makers.authentication.adapter.in.web.dto.auth.response.AuthResponse;
import sopt.makers.authentication.adapter.in.web.util.ResponseUtil;
import sopt.makers.authentication.application.port.in.auth.GetSocialAccountUsecase;
import sopt.makers.authentication.application.port.in.auth.UpdateSocialAccountUsecase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/social/accounts")
@RequiredArgsConstructor
public class SocialAccountApiController implements SocialAccountApi {
  private final GetSocialAccountUsecase getSocialAccountUsecase;
  private final UpdateSocialAccountUsecase updateSocialAccountUsecase;

  @Override
  @GetMapping("/platform")
  public ResponseEntity<BaseResponse<?>> getRegisterSocialAccountPlatform(
      @RequestParam(value = "name", required = false) String name,
      @RequestParam(value = "phone") String phone) {
    GetSocialAccountUsecase.GetSocialAccountPlatformCommand command =
        new GetSocialAccountUsecase.GetSocialAccountPlatformCommand(name, phone);
    GetSocialAccountUsecase.SocialAccountPlatformInfo socialAccountPlatform =
        getSocialAccountUsecase.getSocialAccountPlatform(command);

    return ResponseUtil.success(
        SocialAccountSuccess.GET_SOCIAL_ACCOUNT_PLATFORM,
        AuthResponse.SocialAccountPlatform.from(socialAccountPlatform));
  }

  @Override
  @PatchMapping
  public ResponseEntity<BaseResponse<?>> updateSocialAccount(
      @RequestBody SocialAccountRequest.UpdateSocialAccount socialAccountInfo) {
    updateSocialAccountUsecase.update(socialAccountInfo.toCommand());
    return ResponseUtil.success(SocialAccountSuccess.UPDATE_SOCIAL_ACCOUNT);
  }
}
