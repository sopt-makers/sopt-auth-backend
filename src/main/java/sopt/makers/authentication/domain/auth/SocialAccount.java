package sopt.makers.authentication.domain.auth;

import jakarta.validation.constraints.*;

public record SocialAccount(
    @NotNull String authPlatformId, @NotNull AuthPlatform authPlatformType) {
  public static SocialAccount of(final String authPlatformId, final String authPlatformType) {
    return new SocialAccount(authPlatformId, AuthPlatform.find(authPlatformType));
  }
}
