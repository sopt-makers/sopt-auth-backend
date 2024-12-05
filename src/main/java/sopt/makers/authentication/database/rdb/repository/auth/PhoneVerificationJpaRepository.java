package sopt.makers.authentication.database.rdb.repository.auth;

import sopt.makers.authentication.database.rdb.entity.auth.PhoneVerificationEntity;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface PhoneVerificationJpaRepository extends JpaRepository<PhoneVerificationEntity, Long> {

  Optional<PhoneVerificationEntity> findByPhoneAndCodeAndType(
      String phone, String code, PhoneVerificationType type);

  void deleteByNameAndPhoneAndCodeAndType(
      String name, String phone, String code, PhoneVerificationType type);
}
