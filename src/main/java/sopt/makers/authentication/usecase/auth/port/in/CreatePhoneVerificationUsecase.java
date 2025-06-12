package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;

public interface CreatePhoneVerificationUsecase {

  PhoneVerification create(CreateVerificationCommand command);

  record CreateVerificationCommand(
      Long userId, String phone, PhoneVerificationType verificationType) {}
}
