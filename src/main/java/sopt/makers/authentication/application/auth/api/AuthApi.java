package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.request.Create;
import sopt.makers.authentication.application.auth.dto.request.Verify;
import sopt.makers.authentication.support.common.api.BaseResponse;

import org.springframework.http.ResponseEntity;

public interface AuthApi {

  ResponseEntity<BaseResponse<?>> createPhoneVerification(
      Create.PhoneVerification phoneVerification);

  ResponseEntity<BaseResponse<?>> verifyPhoneVerification(
      Verify.PhoneVerification phoneVerification);
}
