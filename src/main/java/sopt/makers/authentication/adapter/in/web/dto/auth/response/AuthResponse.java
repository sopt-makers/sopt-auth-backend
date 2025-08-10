package sopt.makers.authentication.adapter.in.web.dto.auth.response;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.application.port.in.auth.GetSocialAccountUsecase;
import sopt.makers.authentication.application.port.in.auth.VerifyPhoneVerificationUsecase;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthResponse {

  public record VerifyResult(
      @JsonProperty("name") String name, @JsonProperty("phone") String phone) {
    public static VerifyResult from(
        VerifyPhoneVerificationUsecase.VerifyVerificationResult result) {
      return new VerifyResult(result.targetName(), result.targetPhone());
    }
  }

  public record AuthenticateSocialAuthInfoForWeb(String accessToken, boolean hasProfile) {
    public static AuthenticateSocialAuthInfoForWeb of(String accessToken, boolean hasProfile) {
      return new AuthenticateSocialAuthInfoForWeb(accessToken, hasProfile);
    }
  }

  public record AuthenticateSocialAuthInfoForApp(
      String accessToken, String refreshToken, boolean hasProfile) {
    public static AuthenticateSocialAuthInfoForApp of(
        String accessToken, String refreshToken, boolean hasProfile) {
      return new AuthenticateSocialAuthInfoForApp(accessToken, refreshToken, hasProfile);
    }
  }

  public record AuthenticateAuthInfoForWeb(String accessToken) {
    public static AuthenticateAuthInfoForWeb of(String accessToken) {
      return new AuthenticateAuthInfoForWeb(accessToken);
    }
  }

  public record AuthenticateAuthInfoForApp(String accessToken, String refreshToken) {
    public static AuthenticateAuthInfoForApp of(String accessToken, String refreshToken) {
      return new AuthenticateAuthInfoForApp(accessToken, refreshToken);
    }
  }

  public record SocialAccountPlatform(@JsonProperty("platform") String platformName) {
    public static SocialAccountPlatform from(
        GetSocialAccountUsecase.SocialAccountPlatformInfo info) {
      return new SocialAccountPlatform(info.platformName());
    }
  }
}
