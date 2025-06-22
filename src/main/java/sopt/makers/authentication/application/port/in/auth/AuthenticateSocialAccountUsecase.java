package sopt.makers.authentication.application.port.in.auth;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface AuthenticateSocialAccountUsecase {
  AuthenticateTokenInfo authenticate(AuthenticateSocialAccountCommand command);

  AuthenticateTokenInfo refresh(AuthenticateTokenInfo command);

  record AuthenticateTokenInfo(String accessToken, String refreshToken) {
    public static AuthenticateTokenInfo of(String accessToken, String refreshToken) {
      return new AuthenticateTokenInfo(accessToken, refreshToken);
    }
  }

  record AuthenticateSocialAccountCommand(String token, AuthPlatform authPlatform) {
    public static AuthenticateSocialAccountCommand of(String token, AuthPlatform authPlatform) {
      return new AuthenticateSocialAccountCommand(token, authPlatform);
    }
  }
}
