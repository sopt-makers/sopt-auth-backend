package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.out.OAuthPlatformPort;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

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
