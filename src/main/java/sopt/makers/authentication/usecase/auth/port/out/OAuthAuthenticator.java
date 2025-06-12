package sopt.makers.authentication.usecase.auth.port.out;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface OAuthAuthenticator {
  String getIdentifier(String idToken, AuthPlatform platform);
}
