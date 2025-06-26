package sopt.makers.authentication.application.port.out.auth;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface OAuthAuthenticator {
  String getIdentifier(String idToken, AuthPlatform platform);
}
