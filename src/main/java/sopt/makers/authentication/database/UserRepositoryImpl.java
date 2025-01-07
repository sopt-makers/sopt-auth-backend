package sopt.makers.authentication.database;

import sopt.makers.authentication.database.rdb.entity.*;
import sopt.makers.authentication.database.rdb.repository.UserRegister;
import sopt.makers.authentication.database.rdb.repository.UserRetriever;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;

import org.springframework.stereotype.Repository;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
  private final UserRetriever userRetriever;
  private final UserRegister userRegister;

  @Override
  public User findBySocialAccount(SocialAccount socialAccount) {
    return userRetriever.findBySocialAccount(socialAccount);
  }

  @Override
  public Long findIdByUser(User user) {
    return userRetriever.findIdByUser(user);
  }

  @Override
  public void save(User user) {
    UserEntity userEntity = UserEntity.fromDomain(user);
    userRegister.save(userEntity);
  }

  @Override
  public User findByPhone(String phone) {
    return userRetriever.findByPhone(phone);
  }

  @Override
  public User update(Long id, User user, SocialAccount socialAccount) {
    user.updateSocialAccount(socialAccount);
    UserEntity userEntity = UserEntity.fromDomain(user);
    userEntity.setId(id);
    UserEntity updatedUserEntity = userRegister.save(userEntity);
    return updatedUserEntity.toDomain();
  }
}
