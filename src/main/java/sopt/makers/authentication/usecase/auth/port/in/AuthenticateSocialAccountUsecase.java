package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface AuthenticateSocialAccountUsecase {
  AuthenticateTokenInfo authenticate(AuthenticateSocialAccountCommand command);

  record AuthenticateTokenInfo(String accessToken, String refreshToken) {
    public static AuthenticateTokenInfo of(String accessToken, String refreshToken) {
      return new AuthenticateTokenInfo(accessToken, refreshToken);
    }
  }

  record AuthenticateSocialAccountCommand(AuthPlatform authPlatform, String code) {}
}
