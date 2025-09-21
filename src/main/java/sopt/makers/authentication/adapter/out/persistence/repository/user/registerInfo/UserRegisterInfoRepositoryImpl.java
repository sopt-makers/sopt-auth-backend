package sopt.makers.authentication.adapter.out.persistence.repository.user.registerInfo;

import sopt.makers.authentication.adapter.out.persistence.entity.UserRegisterInfoEntity;
import sopt.makers.authentication.application.port.out.user.UserRegisterInfoRepository;
import sopt.makers.authentication.domain.user.UserRegisterInfo;

import java.util.Optional;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserRegisterInfoRepositoryImpl implements UserRegisterInfoRepository {

  private final UserRegisterInfoRetriever retriever;
  private final UserRegisterInfoRemover remover;
  private final UserRegisterInfoRegister register;

  @Override
  public Optional<UserRegisterInfo> findByPhone(String phone) {
    return retriever.findByPhone(phone).map(UserRegisterInfoEntity::toDomain);
  }

  @Transactional
  @Override
  public void save(UserRegisterInfo userRegisterInfo) {
    UserRegisterInfoEntity entity = UserRegisterInfoEntity.fromDomain(userRegisterInfo);
    register.save(entity);
  }

  @Transactional
  @Override
  public void delete(UserRegisterInfo userRegisterInfo) {
    Optional<UserRegisterInfoEntity> registerInfoEntity =
        retriever.findByPhone(userRegisterInfo.getPhone());
    registerInfoEntity.ifPresent(remover::remove);
  }
}
