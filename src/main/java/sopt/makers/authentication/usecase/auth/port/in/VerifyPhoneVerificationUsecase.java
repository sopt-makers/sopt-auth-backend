package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.PhoneVerificationType;

public interface VerifyPhoneVerificationUsecase {

  VerifyVerificationResult verify(VerifyVerificationCommand command);

  record VerifyVerificationCommand(
      String name, String phone, String code, PhoneVerificationType verificationType) {}

  record VerifyVerificationResult(boolean isSuccess, String targetName, String targetPhone) {}
}
