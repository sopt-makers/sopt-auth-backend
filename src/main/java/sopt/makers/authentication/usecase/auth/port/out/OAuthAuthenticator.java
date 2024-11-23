package sopt.makers.authentication.usecase.auth.port.out;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface OAuthAuthenticator {
  String getAuthPlatformId(AuthPlatform authPlatform, String code);
}
