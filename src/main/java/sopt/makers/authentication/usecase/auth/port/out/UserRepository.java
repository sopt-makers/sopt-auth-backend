package sopt.makers.authentication.usecase.auth.port.out;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;

public interface UserRepository {

  User findBySocialAccount(SocialAccount socialAccount);

  User findByPhone(String phone);

  Long findIdByUser(User user);

  void save(User user);

  User update(Long id, User user, SocialAccount socialAccount);
}
