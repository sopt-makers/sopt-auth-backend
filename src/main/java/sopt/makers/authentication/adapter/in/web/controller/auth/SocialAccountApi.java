package sopt.makers.authentication.adapter.in.web.controller.auth;

import sopt.makers.authentication.adapter.in.web.dto.auth.request.SocialAccountRequest;
import sopt.makers.authentication.support.common.api.BaseResponse;

import org.springframework.http.ResponseEntity;

public interface SocialAccountApi {
  ResponseEntity<BaseResponse<?>> updateSocialAccount(
      SocialAccountRequest.UpdateSocialAccount socialAccountInfo);

  ResponseEntity<BaseResponse<?>> getRegisterSocialAccountPlatform(String name, String phone);
}
