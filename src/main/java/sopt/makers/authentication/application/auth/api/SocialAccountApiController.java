package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.request.SocialAccountRequest;
import sopt.makers.authentication.support.code.domain.success.SocialAccountSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.util.ResponseUtil;
import sopt.makers.authentication.usecase.auth.port.in.UpdateSocialAccountUsecase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/social/accounts")
@RequiredArgsConstructor
public class SocialAccountApiController implements SocialAccountApi {
  private final UpdateSocialAccountUsecase updateSocialAccountUsecase;

  @Override
  @PatchMapping
  public ResponseEntity<BaseResponse<?>> updateSocialAccount(
      SocialAccountRequest.UpdateSocialAccount socialAccountInfo) {
    updateSocialAccountUsecase.update(socialAccountInfo.toCommand());
    return ResponseUtil.success(SocialAccountSuccess.UPDATE_SOCIAL_ACCOUNT);
  }
}
