package sopt.makers.authentication.application.port.in.auth;

import sopt.makers.authentication.domain.auth.PhoneVerificationType;

public interface CreatePhoneVerificationUsecase {

  void create(CreateVerificationCommand command);

  record CreateVerificationCommand(
      Long userId, String phone, PhoneVerificationType verificationType) {}
}
