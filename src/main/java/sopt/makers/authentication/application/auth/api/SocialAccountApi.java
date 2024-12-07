package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.request.SocialAccountRequest;
import sopt.makers.authentication.support.common.api.BaseResponse;

import org.springframework.http.ResponseEntity;

public interface SocialAccountApi {
  ResponseEntity<BaseResponse<?>> updateSocialAccount(
      SocialAccountRequest.UpdateSocialAccount socialAccountInfo);
}
