package sopt.makers.authentication.usecase.auth.port.out;

import sopt.makers.authentication.domain.auth.*;

public interface OAuthPlatformPort {
  String getAuthPlatformId(AuthPlatform authPlatform, String code);
}
