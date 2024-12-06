package sopt.makers.authentication.application.user.dto.response;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.usecase.user.port.in.GetSocialAccountPlatform;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class UserResponse {

  public record SocialAccountPlatform(@JsonProperty("platform") String platformName) {
    public static SocialAccountPlatform from(
        GetSocialAccountPlatform.SocialAccountPlatformInfo info) {
      return new SocialAccountPlatform(info.platformName());
    }
  }
}
