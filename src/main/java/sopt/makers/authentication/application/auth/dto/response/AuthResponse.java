package sopt.makers.authentication.application.auth.dto.response;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthResponse {
  public record AuthenticateSocialAuthInfoForWeb(String accessToken) {
    public static AuthenticateSocialAuthInfoForWeb of(String accessToken) {
      return new AuthenticateSocialAuthInfoForWeb(accessToken);
    }
  }

  public record AuthenticateSocialAuthInfoForApp(String accessToken, String refreshToken) {
    public static AuthenticateSocialAuthInfoForApp of(String accessToken, String refreshToken) {
      return new AuthenticateSocialAuthInfoForApp(accessToken, refreshToken);
    }
  }
}
