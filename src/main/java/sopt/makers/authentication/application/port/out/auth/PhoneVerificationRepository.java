package sopt.makers.authentication.application.port.out.auth;

import sopt.makers.authentication.domain.auth.PhoneVerification;

public interface PhoneVerificationRepository {

  PhoneVerification create(PhoneVerification phoneVerification);

  PhoneVerification findByPhoneVerification(PhoneVerification phoneVerification);

  void deleteByPhoneVerification(PhoneVerification phoneVerification);

  void update(PhoneVerification phoneVerification);
}
