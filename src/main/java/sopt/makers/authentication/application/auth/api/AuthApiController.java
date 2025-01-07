package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.request.AuthRequest;
import sopt.makers.authentication.application.auth.dto.response.AuthResponse;
import sopt.makers.authentication.support.code.domain.success.AuthSuccess;
import sopt.makers.authentication.support.common.api.BaseResponse;
import sopt.makers.authentication.support.util.CookieUtil;
import sopt.makers.authentication.support.util.ResponseUtil;
import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase.AuthenticateTokenInfo;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.in.SignUpUsecase;
import sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthApiController implements AuthApi {

  private final CreatePhoneVerificationUsecase createVerificationUsecase;
  private final VerifyPhoneVerificationUsecase verifyVerificationUsecase;
  private final AuthenticateSocialAccountUsecase authenticateSocialAccountUsecase;
  private final SignUpUsecase signUpUsecase;
  private final CookieUtil cookieUtil;

  @Override
  @PostMapping("/phone")
  public ResponseEntity<BaseResponse<?>> createPhoneVerification(
      @RequestBody AuthRequest.CreatePhoneVerification createPhoneVerificationRequest) {
    createVerificationUsecase.create(createPhoneVerificationRequest.toCommand());
    return ResponseUtil.success(AuthSuccess.CREATE_PHONE_VERIFICATION);
  }

  @Override
  @PostMapping(value = "/verify/phone")
  public ResponseEntity<BaseResponse<?>> verifyPhoneVerification(
      @RequestBody AuthRequest.VerifyPhoneVerification phoneVerification) {
    VerifyPhoneVerificationUsecase.VerifyVerificationResult result =
        verifyVerificationUsecase.verify(phoneVerification.toCommand());
    return ResponseUtil.success(
        AuthSuccess.VERIFY_PHONE_VERIFICATION, AuthResponse.VerifyResult.from(result));
  }

  @Override
  @PostMapping("/login/web")
  public ResponseEntity<BaseResponse<?>> authenticateSocialAuthInfoFromWeb(
      AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo) {
    AuthenticateTokenInfo tokenInfo =
        authenticateSocialAccountUsecase.authenticate(socialAuthInfo.toCommand());
    HttpHeaders headers = cookieUtil.setRefreshToken(tokenInfo.refreshToken());

    return ResponseUtil.success(
        AuthSuccess.AUTHENTICATE_SOCIAL_ACCOUNT,
        headers,
        AuthResponse.AuthenticateSocialAuthInfoForWeb.of(tokenInfo.accessToken()));
  }

  @Override
  @PostMapping("/login/app")
  public ResponseEntity<BaseResponse<?>> authenticateSocialAuthInfoFromApp(
      AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo) {
    AuthenticateTokenInfo tokenInfo =
        authenticateSocialAccountUsecase.authenticate(socialAuthInfo.toCommand());

    return ResponseUtil.success(
        AuthSuccess.AUTHENTICATE_SOCIAL_ACCOUNT,
        AuthResponse.AuthenticateSocialAuthInfoForApp.of(
            tokenInfo.accessToken(), tokenInfo.refreshToken()));
  }

  @Override
  @PostMapping("/signup")
  public ResponseEntity<BaseResponse<?>> signUp(AuthRequest.SignUp signUp) {
    signUpUsecase.signUp(signUp.toCommand());
    return ResponseUtil.success(AuthSuccess.CREATE_SIGN_UP_USER);
  }
}
