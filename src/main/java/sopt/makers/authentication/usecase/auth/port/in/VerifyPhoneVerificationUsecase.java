package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.application.auth.dto.response.AuthResponse.VerifyResult;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;

public interface VerifyPhoneVerificationUsecase {

  VerifyResult verify(VerifyVerificationCommand command);

  record VerifyVerificationCommand(
      String name, String phone, String code, PhoneVerificationType verificationType) {}
}
