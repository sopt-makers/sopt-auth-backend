package sopt.makers.authentication.database.rdb.repository;

import sopt.makers.authentication.database.rdb.entity.PhoneVerificationEntity;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PhoneVerificationRepositoryImpl implements PhoneVerificationRepository {

  private final PhoneVerificationJpaRepository jpaRepository;

  @Override
  public PhoneVerification save(PhoneVerification phoneVerification) {
    PhoneVerificationEntity createdEntity =
        jpaRepository.save(PhoneVerificationEntity.fromDomain(phoneVerification));
    return createdEntity.toDomain();
  }

  @Override
  public PhoneVerification findByPhoneVerification(PhoneVerification phoneVerification) {
    PhoneVerificationEntity phoneVerificationEntity =
        jpaRepository.findByPhoneVerification(phoneVerification);
    return phoneVerificationEntity.toDomain();
  }

  @Override
  public void deletedByPhoneVerification(PhoneVerification phoneVerification) {
    jpaRepository.deleteByVerification(phoneVerification);
  }
}
