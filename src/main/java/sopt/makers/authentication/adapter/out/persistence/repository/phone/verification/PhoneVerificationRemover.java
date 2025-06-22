package sopt.makers.authentication.adapter.out.persistence.repository.phone.verification;

import sopt.makers.authentication.domain.auth.PhoneVerification;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhoneVerificationRemover {

  private final PhoneVerificationJpaRepository jpaRepository;

  public void remove(PhoneVerification phoneVerification) {
    jpaRepository.deleteByNameAndPhoneAndCodeAndType(
        phoneVerification.getName(),
        phoneVerification.getPhone(),
        phoneVerification.getVerificationCode().getCode(),
        phoneVerification.getVerificationType());
  }
}
