package sopt.makers.authentication.usecase.auth.port.out;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface OAuthAuthenticator {
  String getAuthPlatformId(String authPlatform, String code);

  String getIdentifier(String idToken, AuthPlatform platform);
}
