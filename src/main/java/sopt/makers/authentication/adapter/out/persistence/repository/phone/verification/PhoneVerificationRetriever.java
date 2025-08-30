package sopt.makers.authentication.adapter.out.persistence.repository.phone.verification;

import sopt.makers.authentication.adapter.out.persistence.entity.PhoneVerificationEntity;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.exception.AuthException;
import sopt.makers.authentication.domain.auth.exception.AuthFailure;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhoneVerificationRetriever {

  private final PhoneVerificationJpaRepository jpaRepository;

  public PhoneVerificationEntity find(PhoneVerification phoneVerification) {
    return jpaRepository
        .findTopByPhoneAndTypeOrderByCreatedAtDesc(
            phoneVerification.getPhone(), phoneVerification.getVerificationType())
        .orElseThrow(() -> new AuthException(AuthFailure.NOT_FOUND_PHONE_VERIFICATION));
  }
}
