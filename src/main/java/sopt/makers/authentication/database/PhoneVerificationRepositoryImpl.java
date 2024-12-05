package sopt.makers.authentication.database;

import sopt.makers.authentication.database.rdb.entity.auth.PhoneVerificationEntity;
import sopt.makers.authentication.database.rdb.repository.auth.PhoneVerificationRegister;
import sopt.makers.authentication.database.rdb.repository.auth.PhoneVerificationRemover;
import sopt.makers.authentication.database.rdb.repository.auth.PhoneVerificationRetriever;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class PhoneVerificationRepositoryImpl implements PhoneVerificationRepository {

  private final PhoneVerificationRegister register;
  private final PhoneVerificationRetriever retriever;
  private final PhoneVerificationRemover remover;

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
  public void deletedByPhoneVerification(PhoneVerification phoneVerification) {
    remover.remove(phoneVerification);
  }
}
