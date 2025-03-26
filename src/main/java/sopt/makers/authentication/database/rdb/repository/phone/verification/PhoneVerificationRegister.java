package sopt.makers.authentication.database.rdb.repository.phone.verification;

import sopt.makers.authentication.database.rdb.entity.PhoneVerificationEntity;
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
}
