package sopt.makers.authentication.application.port.in.auth;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface AuthenticateSocialAccountUsecase {
  AuthenticateSocialTokenInfo authenticate(AuthenticateSocialAccountCommand command);

  AuthenticateTokenInfo refresh(AuthenticateTokenInfo command);

  record AuthenticateTokenInfo(String accessToken, String refreshToken) {
    public static AuthenticateTokenInfo of(String accessToken, String refreshToken) {
      return new AuthenticateTokenInfo(accessToken, refreshToken);
    }
  }

  record AuthenticateSocialTokenInfo(
      String accessToken, String refreshToken, boolean isFirstLogin) {
    public static AuthenticateSocialTokenInfo of(
        String accessToken, String refreshToken, boolean isFirstLogin) {
      return new AuthenticateSocialTokenInfo(accessToken, refreshToken, isFirstLogin);
    }
  }

  record AuthenticateSocialAccountCommand(String token, AuthPlatform authPlatform) {
    public static AuthenticateSocialAccountCommand of(String token, AuthPlatform authPlatform) {
      return new AuthenticateSocialAccountCommand(token, authPlatform);
    }
  }
}
