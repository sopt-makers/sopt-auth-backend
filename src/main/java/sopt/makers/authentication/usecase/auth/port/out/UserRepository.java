package sopt.makers.authentication.usecase.auth.port.out;

import sopt.makers.authentication.domain.auth.*;
import sopt.makers.authentication.domain.user.*;

public interface UserRepository {
  User findBySocialAccount(SocialAccount socialAccount);
}
