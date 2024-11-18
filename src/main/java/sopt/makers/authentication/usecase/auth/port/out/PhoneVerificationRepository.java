package sopt.makers.authentication.usecase.auth.port.out;

import sopt.makers.authentication.domain.auth.PhoneVerification;

public interface PhoneVerificationRepository {

  PhoneVerification save(PhoneVerification phoneVerification);

  PhoneVerification findByPhoneVerification(PhoneVerification phoneVerification);

  void deletedByPhoneVerification(PhoneVerification phoneVerification);
}
