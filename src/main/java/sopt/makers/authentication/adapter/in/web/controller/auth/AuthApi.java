package sopt.makers.authentication.adapter.in.web.controller.auth;

import sopt.makers.authentication.adapter.in.web.common.BaseResponse;
import sopt.makers.authentication.adapter.in.web.dto.auth.request.AuthRequest;

import org.springframework.http.ResponseEntity;

public interface AuthApi {

  ResponseEntity<BaseResponse<?>> createPhoneVerification(
      AuthRequest.CreatePhoneVerification phoneVerification);

  ResponseEntity<BaseResponse<?>> verifyPhoneVerification(
      AuthRequest.VerifyPhoneVerification phoneVerification);

  ResponseEntity<BaseResponse<?>> authenticateSocialAuthInfoFromWeb(
      AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo);

  ResponseEntity<BaseResponse<?>> authenticateSocialAuthInfoFromApp(
      AuthRequest.AuthenticateSocialAuthInfo socialAuthInfo);

  ResponseEntity<BaseResponse<?>> refreshTokenFromApp(
      AuthRequest.AuthenticationTokenInfo authenticationTokenInfo);

  ResponseEntity<BaseResponse<?>> refreshTokenFromWeb(String accessToken, String refreshToken);

  ResponseEntity<BaseResponse<?>> signUp(AuthRequest.SignUpInfo signUp);

  ResponseEntity<BaseResponse<?>> withdraw(String accessToken);
}
