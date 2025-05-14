package sopt.makers.authentication.usecase.user.port.out;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;

import java.util.List;

public interface UserRepository {

  User findBySocialAccount(SocialAccount socialAccount);

  User findByPhone(String phone);

  User findById(Long id);

  List<User> findAllById(List<Long> userIds);

  User save(User user);

  void update(User user, SocialAccount socialAccount);

  boolean existsByPhone(String phone);
}
