package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticateSocialAccountService implements AuthenticateSocialAccountUsecase {
  private final OAuthAuthenticator oAuthAuthenticator;

  @Override
  public SocialAccountInfo authenticate(AuthenticateSocialAccountCommand command) {
    String authPlatformId =
        oAuthAuthenticator.getAuthPlatformId(command.authPlatform(), command.code());

    return SocialAccountInfo.of(authPlatformId, command.authPlatform().name());
  }
}
