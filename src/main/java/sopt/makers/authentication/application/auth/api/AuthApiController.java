package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.request.AuthRequest;
import sopt.makers.authentication.application.auth.dto.response.AuthResponse;
import sopt.makers.authentication.support.code.domain.success.AuthSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController implements AuthApi {

  private final CreatePhoneVerificationUsecase createVerificationUsecase;
  private final VerifyPhoneVerificationUsecase verifyVerificationUsecase;

  @Override
  @PostMapping("/phone")
  public ResponseEntity<BaseResponse<?>> createPhoneVerification(
      AuthRequest.CreatePhoneVerification createPhoneVerificationRequest) {
    createVerificationUsecase.create(createPhoneVerificationRequest.toCommand());
    return ResponseEntity.status(AuthSuccess.CREATE_PHONE_VERIFICATION.getStatus().value())
        .body(BaseResponse.ofSuccess(AuthSuccess.CREATE_PHONE_VERIFICATION));
  }

  @Override
  @PostMapping("/verify/phone")
  public ResponseEntity<BaseResponse<?>> verifyPhoneVerification(
      AuthRequest.VerifyPhoneVerification phoneVerification) {
    boolean result = verifyVerificationUsecase.verify(phoneVerification.toCommand());
    return ResponseEntity.status(AuthSuccess.CREATE_PHONE_VERIFICATION.getStatus().value())
        .body(
            BaseResponse.ofSuccess(
                AuthSuccess.CREATE_PHONE_VERIFICATION, new AuthResponse.VerifyResult(result)));
  }
}
