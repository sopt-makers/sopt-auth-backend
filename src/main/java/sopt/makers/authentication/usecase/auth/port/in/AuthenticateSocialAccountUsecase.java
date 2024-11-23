package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface AuthenticateSocialAccountUsecase {
  SocialAccountInfo authenticate(AuthenticateSocialAccountCommand command);

  record SocialAccountInfo(String authPlatformId, String authPlatformType) {
    public static SocialAccountInfo of(String authPlatformId, String authPlatformType) {
      return new SocialAccountInfo(authPlatformId, authPlatformType);
    }
  }

  record AuthenticateSocialAccountCommand(AuthPlatform authPlatform, String code) {}
}
