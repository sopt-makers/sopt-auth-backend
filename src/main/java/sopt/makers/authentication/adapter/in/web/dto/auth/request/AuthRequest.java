package sopt.makers.authentication.adapter.in.web.dto.auth.request;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.application.port.in.auth.AuthenticateSocialAccountUsecase.AuthenticateSocialAccountCommand;
import sopt.makers.authentication.application.port.in.auth.AuthenticateSocialAccountUsecase.AuthenticateTokenInfo;
import sopt.makers.authentication.application.port.in.auth.CreatePhoneVerificationUsecase.CreateVerificationCommand;
import sopt.makers.authentication.application.port.in.auth.SignUpUsecase.SignUpCommand;
import sopt.makers.authentication.application.port.in.auth.VerifyPhoneVerificationUsecase.VerifyVerificationCommand;
import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthRequest {

  public record CreatePhoneVerification(
      @JsonProperty("userId") Long userId,
      @JsonProperty("phone") String number,
      @JsonProperty("type") String verificationTypeName) {
    public CreateVerificationCommand toCommand() {
      return new CreateVerificationCommand(
          this.userId, this.number, PhoneVerificationType.valueOf(this.verificationTypeName));
    }
  }

  public record VerifyPhoneVerification(
      @JsonProperty("name") String name,
      @JsonProperty("phone") String number,
      @JsonProperty("code") String code,
      @JsonProperty("type") String verificationTypeName) {
    public VerifyVerificationCommand toCommand() {
      return new VerifyVerificationCommand(
          this.name,
          this.number,
          this.code,
          PhoneVerificationType.valueOf(this.verificationTypeName));
    }
  }

  public record AuthenticateSocialAuthInfo(
      @JsonProperty("token") String token, @JsonProperty("authPlatform") String authPlatform) {
    public AuthenticateSocialAccountCommand toCommand() {
      return AuthenticateSocialAccountCommand.of(this.token, AuthPlatform.find(this.authPlatform));
    }
  }

  public record SignUpInfo(
      @JsonProperty("name") String name,
      @JsonProperty("phone") String phone,
      @JsonProperty("token") String token,
      @JsonProperty("authPlatform") String authPlatform) {
    public SignUpCommand toCommand() {
      return new SignUpCommand(
          this.name, this.phone, this.token, AuthPlatform.find(this.authPlatform));
    }
  }

  public record AuthenticationTokenInfo(String accessToken, String refreshToken) {
    public AuthenticateTokenInfo toCommand() {
      return AuthenticateTokenInfo.of(accessToken, refreshToken);
    }
  }
}
