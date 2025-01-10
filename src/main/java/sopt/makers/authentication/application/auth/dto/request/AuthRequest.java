package sopt.makers.authentication.application.auth.dto.request;

import static lombok.AccessLevel.PRIVATE;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase.AuthenticateSocialAccountCommand;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase.CreateVerificationCommand;
import sopt.makers.authentication.usecase.auth.port.in.SignUpUsecase.SignUpCommand;
import sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase.VerifyVerificationCommand;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthRequest {

  public record CreatePhoneVerification(
      @JsonProperty("name") String name,
      @JsonProperty("phone") String number,
      @JsonProperty("type") String verificationTypeName) {
    public CreateVerificationCommand toCommand() {
      return new CreateVerificationCommand(
          this.name, this.number, PhoneVerificationType.valueOf(this.verificationTypeName));
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
}
