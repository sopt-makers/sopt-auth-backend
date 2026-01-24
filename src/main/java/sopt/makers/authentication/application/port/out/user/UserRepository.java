package sopt.makers.authentication.application.port.out.user;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserRepository {

  User findBySocialAccount(SocialAccount socialAccount);

  User findById(Long userId);

  User findByIdWithHistories(Long userId);

  User findByPhone(String phone);

  List<User> findAllById(List<Long> userIds);

  User save(User user);

  void update(User user, SocialAccount socialAccount);

  void update(User user, Profile profile);

  void update(User user);

  boolean existsByPhone(String phone);

  Page<User> findAllByGenerationAndPartAndNameAndTeam(
      Integer generation, Part part, String name, Team team, Boolean isAdmin, Pageable pageable);

  int countByGenerationAndIsSopt(int generation, boolean isSopt);
}
