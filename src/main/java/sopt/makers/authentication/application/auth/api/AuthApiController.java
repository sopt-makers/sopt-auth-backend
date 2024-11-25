package sopt.makers.authentication.application.auth.api;

import static sopt.makers.authentication.support.code.domain.success.AuthSuccess.AUTHENTICATE_SOCIAL_ACCOUNT;

import sopt.makers.authentication.application.auth.dto.request.AuthRequest;
import sopt.makers.authentication.application.auth.dto.response.AuthResponse;
import sopt.makers.authentication.support.code.domain.success.AuthSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.util.CookieUtil;
import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase.AuthenticateTokenInfo;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;

import org.springframework.http.HttpHeaders;
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
  private final AuthenticateSocialAccountUsecase authenticateSocialAccountUsecase;
  private final CookieUtil cookieUtil;

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
  public ResponseEntity<BaseResponse<?>> authenticateSocialAuthInfoFromWeb(
      AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo) {
    AuthenticateTokenInfo tokenInfo =
        authenticateSocialAccountUsecase.authenticate(socialAuthInfo.toCommand());
    HttpHeaders headers = cookieUtil.setRefreshToken(tokenInfo.refreshToken());

    return ResponseEntity.ok()
        .headers(headers)
        .body(
            BaseResponse.ofSuccess(
                AUTHENTICATE_SOCIAL_ACCOUNT,
                AuthResponse.AuthenticateSocialAuthInfoForWeb.of(tokenInfo.accessToken())));
  }

  @Override
  @PostMapping("/app/login")
  public ResponseEntity<BaseResponse<?>> authenticateSocialAuthInfoFromApp(
      AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo) {
    AuthenticateTokenInfo tokenInfo =
        authenticateSocialAccountUsecase.authenticate(socialAuthInfo.toCommand());

    return ResponseEntity.ok(
        BaseResponse.ofSuccess(
            AUTHENTICATE_SOCIAL_ACCOUNT,
            AuthResponse.AuthenticateSocialAuthInfoForApp.of(
                tokenInfo.accessToken(), tokenInfo.refreshToken())));
  }
}
