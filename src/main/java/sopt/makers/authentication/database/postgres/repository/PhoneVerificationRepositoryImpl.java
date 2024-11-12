package sopt.makers.authentication.database.postgres.repository;

import sopt.makers.authentication.database.postgres.entity.PhoneVerificationEntity;
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
        jpaRepository.save(PhoneVerificationEntity.from(phoneVerification));
    return createdEntity.toDomain();
  }
}
