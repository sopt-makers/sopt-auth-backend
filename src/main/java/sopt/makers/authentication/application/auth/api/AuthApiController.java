package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.request.Create;
import sopt.makers.authentication.application.auth.dto.request.Verify;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.usecase.auth.port.in.CreateVerificationUsecase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController implements AuthApi {

  private final CreateVerificationUsecase createVerificationUsecase;

  @Override
  @PostMapping("/phone")
  public ResponseEntity<BaseResponse<?>> createPhoneVerification(
      Create.PhoneVerification phoneVerify) {
    return null;
  }

  @Override
  @PostMapping("/verify/phone")
  public ResponseEntity<BaseResponse<?>> verifyPhoneVerification(
      Verify.PhoneVerification phoneVerification) {
    return null;
  }
}
