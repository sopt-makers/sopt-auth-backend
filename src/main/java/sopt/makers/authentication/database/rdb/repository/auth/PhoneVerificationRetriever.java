package sopt.makers.authentication.database.rdb.repository.auth;

import sopt.makers.authentication.database.rdb.entity.auth.PhoneVerificationEntity;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.support.code.domain.failure.AuthFailure;
import sopt.makers.authentication.support.exception.domain.AuthException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhoneVerificationRetriever {

  private final PhoneVerificationJpaRepository jpaRepository;

  public PhoneVerificationEntity find(PhoneVerification phoneVerification) {
    return jpaRepository
        .findByPhoneAndCodeAndType(
            phoneVerification.getPhone(),
            phoneVerification.getVerificationCode().getCode(),
            phoneVerification.getVerificationType())
        .orElseThrow(() -> new AuthException(AuthFailure.NOT_FOUND_PHONE_VERIFICATION));
  }
}
