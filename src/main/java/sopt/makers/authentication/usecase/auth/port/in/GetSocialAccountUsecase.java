package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.*;

public interface GetSocialAccountUsecase {
  SocialAccount get(GetSocialAccountCommand command);

  record GetSocialAccountCommand(AuthPlatform authPlatform, String code) {}
}
