package sopt.makers.authentication.application.auth.dto.response;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthResponse {
  public record AuthenticateSocialAuthInfoForWeb(String accessToken) {}

  public record AuthenticateSocialAuthInfoForApp(String accessToken, String refreshToken) {}
}
