package sopt.makers.authentication.usecase.auth.port.in;

public interface AuthenticateSocialAccountUsecase {
  AuthenticateTokenInfo authenticate(AuthenticateSocialAccountCommand command);

  record AuthenticateTokenInfo(String accessToken, String refreshToken) {
    public static AuthenticateTokenInfo of(String accessToken, String refreshToken) {
      return new AuthenticateTokenInfo(accessToken, refreshToken);
    }
  }

  record AuthenticateSocialAccountCommand(String authPlatform, String code) {
    public static AuthenticateSocialAccountCommand of(String authPlatform, String code) {
      return new AuthenticateSocialAccountCommand(authPlatform, code);
    }
  }
}
