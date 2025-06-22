package sopt.makers.authentication.adapter.in.web.controller.auth;

import static sopt.makers.authentication.support.constant.JwtConstant.REFRESH_TOKEN_HEADER;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.adapter.in.web.common.code.AuthSuccess;
import sopt.makers.authentication.adapter.in.web.dto.auth.request.AuthRequest;
import sopt.makers.authentication.adapter.in.web.dto.auth.response.AuthResponse;
import sopt.makers.authentication.application.port.in.auth.AuthenticateSocialAccountUsecase;
import sopt.makers.authentication.application.port.in.auth.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.application.port.in.auth.SignUpUsecase;
import sopt.makers.authentication.application.port.in.auth.VerifyPhoneVerificationUsecase;
import sopt.makers.authentication.support.util.CookieUtil;
import sopt.makers.authentication.support.util.ResponseUtil;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
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
      @RequestBody AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo) {
    AuthenticateSocialAccountUsecase.AuthenticateTokenInfo tokenInfo =
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
      @RequestBody AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo) {
    AuthenticateSocialAccountUsecase.AuthenticateTokenInfo tokenInfo =
        authenticateSocialAccountUsecase.authenticate(socialAuthInfo.toCommand());

    return ResponseUtil.success(
        AuthSuccess.AUTHENTICATE_SOCIAL_ACCOUNT,
        AuthResponse.AuthenticateSocialAuthInfoForApp.of(
            tokenInfo.accessToken(), tokenInfo.refreshToken()));
  }

  @PostMapping("/signup")
  public ResponseEntity<BaseResponse<?>> signUp(@RequestBody AuthRequest.SignUpInfo signUpInfo) {
    signUpUsecase.signUp(signUpInfo.toCommand());
    return ResponseUtil.success(AuthSuccess.CREATE_SIGN_UP_USER);
  }

  @Override
  @PostMapping("/refresh/web")
  public ResponseEntity<BaseResponse<?>> refreshTokenFromWeb(
      @RequestHeader(HttpHeaders.AUTHORIZATION) String accessToken,
      @CookieValue(REFRESH_TOKEN_HEADER) String refreshToken) {
    AuthRequest.AuthenticationTokenInfo authenticationTokenInfo =
        new AuthRequest.AuthenticationTokenInfo(accessToken, refreshToken);

    AuthenticateSocialAccountUsecase.AuthenticateTokenInfo tokenInfo =
        authenticateSocialAccountUsecase.refresh(authenticationTokenInfo.toCommand());
    HttpHeaders headers = cookieUtil.setRefreshToken(tokenInfo.refreshToken());

    return ResponseUtil.success(
        AuthSuccess.AUTHENTICATE_SOCIAL_ACCOUNT,
        headers,
        AuthResponse.AuthenticateSocialAuthInfoForWeb.of(tokenInfo.accessToken()));
  }

  @Override
  @PostMapping("/refresh/app")
  public ResponseEntity<BaseResponse<?>> refreshTokenFromApp(
      AuthRequest.AuthenticationTokenInfo authenticationTokenInfo) {

    AuthenticateSocialAccountUsecase.AuthenticateTokenInfo tokenInfo =
        authenticateSocialAccountUsecase.refresh(authenticationTokenInfo.toCommand());

    return ResponseUtil.success(
        AuthSuccess.AUTHENTICATE_SOCIAL_ACCOUNT,
        AuthResponse.AuthenticateSocialAuthInfoForApp.of(
            tokenInfo.accessToken(), tokenInfo.refreshToken()));
  }
}
