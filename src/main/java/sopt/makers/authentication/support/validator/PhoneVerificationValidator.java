package sopt.makers.authentication.support.validator;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.PHONE_NOT_VERIFIED;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhoneVerificationValidator {
  private final PhoneVerificationRepository phoneVerificationRepository;

  public void validate(String name, String phone, PhoneVerificationType type) {
    boolean isVerified = findPhoneVerification(name, phone, type).isVerified();
    if (!isVerified) {
      throw new AuthException(PHONE_NOT_VERIFIED);
    }
  }

  private PhoneVerification findPhoneVerification(
      String name, String phone, PhoneVerificationType type) {
    PhoneVerification phoneVerification = PhoneVerification.of(name, phone, type, null);
    return phoneVerificationRepository.findLatestByPhoneNameType(phoneVerification);
  }
}
