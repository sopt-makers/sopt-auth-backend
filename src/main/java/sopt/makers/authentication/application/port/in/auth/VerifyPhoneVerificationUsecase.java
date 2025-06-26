package sopt.makers.authentication.application.port.in.auth;

import sopt.makers.authentication.domain.auth.PhoneVerificationType;

public interface VerifyPhoneVerificationUsecase {

  VerifyVerificationResult verify(VerifyVerificationCommand command);

  record VerifyVerificationCommand(
      String name, String phone, String code, PhoneVerificationType verificationType) {}

  record VerifyVerificationResult(String targetName, String targetPhone) {}
}
