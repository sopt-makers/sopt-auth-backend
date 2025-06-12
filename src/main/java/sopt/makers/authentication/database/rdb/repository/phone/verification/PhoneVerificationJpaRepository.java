package sopt.makers.authentication.database.rdb.repository.phone.verification;

import sopt.makers.authentication.database.rdb.entity.PhoneVerificationEntity;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface PhoneVerificationJpaRepository extends JpaRepository<PhoneVerificationEntity, Long> {

  Optional<PhoneVerificationEntity> findTopByPhoneAndTypeOrderByCreatedAtDesc(
      String phone, PhoneVerificationType type);

  void deleteByNameAndPhoneAndCodeAndType(
      String name, String phone, String code, PhoneVerificationType type);

  Optional<PhoneVerificationEntity> findTopByPhoneAndTypeAndNameOrderByCreatedAtDesc(
      String phone, PhoneVerificationType type, String name);
}
