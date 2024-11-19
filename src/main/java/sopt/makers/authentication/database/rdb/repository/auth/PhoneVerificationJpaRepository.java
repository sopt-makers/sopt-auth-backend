package sopt.makers.authentication.database.rdb.repository.auth;

import sopt.makers.authentication.database.rdb.entity.auth.PhoneVerificationEntity;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.support.code.domain.failure.AuthFailure;
import sopt.makers.authentication.support.exception.domain.AuthException;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface PhoneVerificationJpaRepository extends JpaRepository<PhoneVerificationEntity, Long> {

  Optional<PhoneVerificationEntity> findByNameAndPhoneAndCodeAndType(
      String name, String phone, String code, PhoneVerificationType type);

  void deleteByNameAndPhoneAndCodeAndType(
      String name, String phone, String code, PhoneVerificationType type);

  default PhoneVerificationEntity findByPhoneVerification(PhoneVerification verification) {
    PhoneVerificationEntity targetPhoneVerificationEntity =
        findByNameAndPhoneAndCodeAndType(
                verification.getName(),
                verification.getPhone(),
                verification.getVerificationCode().getCode(),
                verification.getVerificationType())
            .orElseThrow(() -> new AuthException(AuthFailure.NOT_FOUND_PHONE_VERIFICATION));
    return targetPhoneVerificationEntity;
  }

  default void deleteByVerification(PhoneVerification verification) {
    deleteByNameAndPhoneAndCodeAndType(
        verification.getName(),
        verification.getPhone(),
        verification.getVerificationCode().getCode(),
        verification.getVerificationType());
  }
}
