package sopt.makers.authentication.database.rdb.repository.auth;

import sopt.makers.authentication.database.rdb.entity.auth.PhoneVerificationEntity;
import sopt.makers.authentication.domain.auth.PhoneVerification;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhoneVerificationRegister {

  private final PhoneVerificationJpaRepository jpaRepository;

  public PhoneVerificationEntity register(PhoneVerification phoneVerification) {
    PhoneVerificationEntity entity = PhoneVerificationEntity.fromDomain(phoneVerification);
    return jpaRepository.save(entity);
  }

  public PhoneVerificationEntity register(final long id, PhoneVerification phoneVerification) {
    PhoneVerificationEntity entity = PhoneVerificationEntity.fromDomain(id, phoneVerification);
    return jpaRepository.save(entity);
  }
}
