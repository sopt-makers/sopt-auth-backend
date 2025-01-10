package sopt.makers.authentication.database;

import sopt.makers.authentication.database.rdb.entity.UserRegisterInfoEntity;
import sopt.makers.authentication.database.rdb.repository.UserRegisterInfoRemover;
import sopt.makers.authentication.database.rdb.repository.UserRegisterInfoRetriever;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRegisterInfoRepositoryImpl implements UserRegisterInfoRepository {

  private final UserRegisterInfoRetriever retriever;
  private final UserRegisterInfoRemover remover;

  @Override
  public UserRegisterInfo findByPhone(String phone) {
    UserRegisterInfoEntity targetRegisterInfo = retriever.findByPhone(phone);
    return targetRegisterInfo.toDomain();
  }

  @Override
  public void delete(UserRegisterInfo userRegisterInfo) {
    UserRegisterInfoEntity registerInfoEntity = retriever.findByPhone(userRegisterInfo.getPhone());
    remover.remove(registerInfoEntity);
  }
}
