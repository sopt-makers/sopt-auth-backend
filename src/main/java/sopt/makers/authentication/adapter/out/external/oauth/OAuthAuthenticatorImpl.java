package sopt.makers.authentication.adapter.out.external.oauth;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticatorImpl implements OAuthAuthenticator {
  private final AppleAuthService appleAuthService;
  private final GoogleAuthService googleAuthService;

  @Override
  public String getIdentifier(String idToken, AuthPlatform platform) {
    return switch (platform) {
      case APPLE -> appleAuthService.getIdentifierByToken(idToken);
      case GOOGLE -> googleAuthService.getIdentifierByToken(idToken);
    };
  }
}
