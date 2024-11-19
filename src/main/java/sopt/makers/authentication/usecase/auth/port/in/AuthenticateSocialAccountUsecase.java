package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.SocialAccount;

public interface AuthenticateSocialAccountUsecase {
  SocialAccount authenticate(AuthenticateSocialAccountCommand command);

  record AuthenticateSocialAccountCommand(AuthPlatform authPlatform, String code) {}
}
