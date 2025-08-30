package sopt.makers.authentication.application.validator.auth;

import static sopt.makers.authentication.domain.auth.exception.AuthFailure.PHONE_NOT_VERIFIED;

import sopt.makers.authentication.application.port.out.auth.PhoneVerificationRepository;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.auth.exception.AuthException;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhoneVerificationValidator {
  private final PhoneVerificationRepository phoneVerificationRepository;

  public void validate(String phone, PhoneVerificationType type) {
    boolean isVerified = findPhoneVerification(phone, type).isVerified();
    if (!isVerified) {
      throw new AuthException(PHONE_NOT_VERIFIED);
    }
  }

  private PhoneVerification findPhoneVerification(String phone, PhoneVerificationType type) {
    PhoneVerification phoneVerification = PhoneVerification.of(null, phone, type, null);
    return phoneVerificationRepository.findByPhoneVerification(phoneVerification);
  }
}
