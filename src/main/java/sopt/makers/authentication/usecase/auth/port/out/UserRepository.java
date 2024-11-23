package sopt.makers.authentication.usecase.auth.port.out;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;

public interface UserRepository {
  User findBySocialAccount(SocialAccount socialAccount);
}
