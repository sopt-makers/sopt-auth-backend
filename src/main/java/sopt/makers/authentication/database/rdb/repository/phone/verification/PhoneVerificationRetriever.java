package sopt.makers.authentication.database.rdb.repository.phone.verification;

import sopt.makers.authentication.database.rdb.entity.PhoneVerificationEntity;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.support.code.domain.failure.AuthFailure;
import sopt.makers.authentication.support.exception.domain.AuthException;

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

  public PhoneVerificationEntity findLatestByPhoneNameType(PhoneVerification phoneVerification) {
    return jpaRepository
        .findTopByPhoneAndTypeAndNameOrderByCreatedAtDesc(
            phoneVerification.getPhone(),
            phoneVerification.getVerificationType(),
            phoneVerification.getName())
        .orElseThrow(() -> new AuthException(AuthFailure.NOT_FOUND_PHONE_VERIFICATION));
  }
}
