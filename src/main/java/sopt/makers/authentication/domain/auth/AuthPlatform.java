package sopt.makers.authentication.domain.auth;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.INVALID_SOCIAL_PLATFORM;

import sopt.makers.authentication.support.exception.domain.AuthException;

import java.util.Arrays;

public enum AuthPlatform {
  GOOGLE,
  APPLE;

  public static AuthPlatform find(final String platform) {

    return Arrays.stream(AuthPlatform.values())
        .filter(p -> p.name().equals(platform))
        .findFirst()
        .orElseThrow(() -> new AuthException(INVALID_SOCIAL_PLATFORM));
  }
}
