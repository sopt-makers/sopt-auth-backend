package sopt.makers.authentication.application.auth.dto.request;

import static lombok.AccessLevel.PRIVATE;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class SocialAccountRequest {
  public record UpdateSocialAccount(String phone, String code, String authPlatform) {}
}
