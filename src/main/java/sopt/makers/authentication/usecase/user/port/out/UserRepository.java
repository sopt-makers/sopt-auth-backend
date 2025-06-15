package sopt.makers.authentication.usecase.user.port.out;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;

import java.util.List;

public interface UserRepository {

  User findBySocialAccount(SocialAccount socialAccount);

  User findById(Long userId);

  User findByPhone(String phone);

  List<User> findAllById(List<Long> userIds);

  User save(User user);

  void update(User user, SocialAccount socialAccount);

  void update(User user, Profile profile);

  boolean existsByPhone(String phone);

  List<User> findAllByActivity(Integer generation, Part part);
}
