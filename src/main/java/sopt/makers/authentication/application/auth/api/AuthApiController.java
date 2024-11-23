package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.request.AuthRequest;
import sopt.makers.authentication.application.auth.dto.response.AuthResponse;
import sopt.makers.authentication.support.code.domain.success.AuthSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;

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
    return null;
  }

  @Override
  @PostMapping("/web/login")
  public ResponseEntity<BaseResponse<AuthResponse.AuthenticateSocialAuthInfoForWeb>>
      authenticateSocialAuthInfoFromWeb(AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo) {
    return null;
  }

  @Override
  @PostMapping("/web/app")
  public ResponseEntity<BaseResponse<AuthResponse.AuthenticateSocialAuthInfoForApp>>
      authenticateSocialAuthInfoFromApp(AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo) {
    return null;
  }
}
