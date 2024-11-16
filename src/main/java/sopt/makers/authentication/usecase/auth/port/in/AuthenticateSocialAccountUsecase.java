package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.*;

public interface AuthenticateSocialAccountUsecase {
  SocialAccount authenticate(AuthenticateSocialAccountCommand command);

  record AuthenticateSocialAccountCommand(AuthPlatform authPlatform, String code) {}
}
