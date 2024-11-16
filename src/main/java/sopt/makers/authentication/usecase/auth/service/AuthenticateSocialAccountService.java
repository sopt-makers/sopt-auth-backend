package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.*;
import sopt.makers.authentication.usecase.auth.port.in.*;
import sopt.makers.authentication.usecase.auth.port.out.*;

import org.springframework.stereotype.*;

import lombok.*;

@Service
@RequiredArgsConstructor
public class AuthenticateSocialAccountService implements AuthenticateSocialAccountUsecase {
  private final OAuthPlatformPort oAuthPlatformPort;

  @Override
  public SocialAccount authenticate(AuthenticateSocialAccountCommand command) {
    String authPlatformId =
        oAuthPlatformPort.getAuthPlatformId(command.authPlatform(), command.code());

    return SocialAccount.of(authPlatformId, command.authPlatform().name());
  }
}
