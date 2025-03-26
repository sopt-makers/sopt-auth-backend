package sopt.makers.authentication.database.rdb.repository.user;

import sopt.makers.authentication.database.rdb.entity.UserEntity;
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
  public void save(User user) {
    UserEntity userEntity = UserEntity.fromDomain(user);
    userRegister.save(userEntity);
  }

  @Override
  public User findByPhone(String phone) {
    return userRetriever.findByPhone(phone);
  }

  @Override
  public void update(User user, SocialAccount socialAccount) {
    user.updateSocialAccount(socialAccount);
    UserEntity userEntity = UserEntity.fromDomain(user);
    userRegister.save(userEntity);
  }
}
