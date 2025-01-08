package sopt.makers.authentication.application.auth.dto.response;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.usecase.auth.port.in.GetSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthResponse {

  public record VerifyResult(
      @JsonProperty("isVerified") boolean isVerified,
      @JsonProperty("name") String name,
      @JsonProperty("phone") String phone) {
    public static VerifyResult from(
        VerifyPhoneVerificationUsecase.VerifyVerificationResult result) {
      return new VerifyResult(result.isSuccess(), result.targetName(), result.targetPhone());
    }
  }

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

  public record AuthenticateSocialAuthInfo(String accessToken, String refreshToken) {
    public static AuthenticateSocialAuthInfo of(String accessToken, String refreshToken) {
      return new AuthenticateSocialAuthInfo(accessToken, refreshToken);
    }
  }

  public record SocialAccountPlatform(@JsonProperty("platform") String platformName) {
    public static SocialAccountPlatform from(
        GetSocialAccountUsecase.SocialAccountPlatformInfo info) {
      return new SocialAccountPlatform(info.platformName());
    }
  }
}
