package sopt.makers.authentication.application.auth.dto.request;

import static lombok.AccessLevel.PRIVATE;
import static sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase.*;

import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase.CreateVerificationCommand;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = PRIVATE)
public final class AuthRequest {

  public record CreatePhoneVerification(String name, String number, String verificationTypeName) {
    public CreateVerificationCommand toCommand() {
      return new CreateVerificationCommand(
          this.name, this.number, PhoneVerificationType.valueOf(this.verificationTypeName));
    }
  }

  public record VerifyPhoneVerification(
      String name, String number, String code, String verificationTypeName) {
    public VerifyVerificationCommand toCommand() {
      return new VerifyVerificationCommand(
          this.name,
          this.number,
          this.code,
          PhoneVerificationType.valueOf(this.verificationTypeName));
    }
  }
}
