package sopt.makers.authentication.application.auth.api;

import sopt.makers.authentication.application.auth.dto.request.AuthRequest;
import sopt.makers.authentication.support.common.api.BaseResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;

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

  ResponseEntity<BaseResponse<?>> refreshTokenFromWeb(
      @RequestHeader("accessToken") String accessToken,
      @RequestHeader("refreshToken") String refreshToken);
}
