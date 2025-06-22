package sopt.makers.authentication.adapter.out.persistence.repository.phone.verification;

import sopt.makers.authentication.adapter.out.persistence.entity.PhoneVerificationEntity;
import sopt.makers.authentication.application.port.out.auth.PhoneVerificationRepository;
import sopt.makers.authentication.domain.auth.PhoneVerification;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PhoneVerificationRepositoryImpl implements PhoneVerificationRepository {

  private final PhoneVerificationRegister register;
  private final PhoneVerificationRetriever retriever;
  private final PhoneVerificationRemover remover;

  @Transactional
  @Override
  public PhoneVerification create(PhoneVerification phoneVerification) {
    PhoneVerificationEntity createdEntity = register.register(phoneVerification);
    return createdEntity.toDomain();
  }

  @Override
  public PhoneVerification findByPhoneVerification(PhoneVerification phoneVerification) {
    PhoneVerificationEntity phoneVerificationEntity = retriever.find(phoneVerification);
    return phoneVerificationEntity.toDomain();
  }

  @Override
  public PhoneVerification findLatestByPhoneNameType(PhoneVerification phoneVerification) {
    PhoneVerificationEntity phoneVerificationEntity =
        retriever.findLatestByPhoneNameType(phoneVerification);
    return phoneVerificationEntity.toDomain();
  }

  @Transactional
  @Override
  public void deleteByPhoneVerification(PhoneVerification phoneVerification) {
    remover.remove(phoneVerification);
  }

  @Transactional
  @Override
  public void update(PhoneVerification phoneVerification) {
    register.register(phoneVerification);
  }
}
