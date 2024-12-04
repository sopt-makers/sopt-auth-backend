package sopt.makers.authentication.database;

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

  @Override
  public User findBySocialAccount(SocialAccount socialAccount) {
    return userRetriever.findBySocialAccount(socialAccount);
  }

  @Override
  public Long findIdByUser(User user) {
    return userRetriever.findIdByUser(user);
  }

  @Override
  public User findByPhone(String phone) {
    return userRetriever.findByPhone(phone);
  }
}
