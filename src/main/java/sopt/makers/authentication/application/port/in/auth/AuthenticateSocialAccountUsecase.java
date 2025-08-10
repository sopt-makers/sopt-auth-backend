package sopt.makers.authentication.application.port.in.auth;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface AuthenticateSocialAccountUsecase {
  AuthenticateTokenInfo authenticate(AuthenticateSocialAccountCommand command);

  AuthenticateTokenInfo refresh(AuthenticateTokenInfo command);

  record AuthenticateTokenInfo(String accessToken, String refreshToken, boolean hasProfile) {
    public static AuthenticateTokenInfo of(
        String accessToken, String refreshToken, boolean hasProfile) {
      return new AuthenticateTokenInfo(accessToken, refreshToken, hasProfile);
    }
  }

  record AuthenticateSocialAccountCommand(String token, AuthPlatform authPlatform) {
    public static AuthenticateSocialAccountCommand of(String token, AuthPlatform authPlatform) {
      return new AuthenticateSocialAccountCommand(token, authPlatform);
    }
  }
}
