package sopt.makers.authentication.external.oauth;

import static sopt.makers.authentication.support.code.external.failure.ClientError.INVALID_ID_TOKEN;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.support.exception.external.ClientResponseException;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthAuthenticatorImpl implements OAuthAuthenticator {
  private final AppleAuthService appleAuthService;
  private final GoogleAuthService googleAuthService;

  @Override
  public String getAuthPlatformId(String authPlatform, String code) {
    String idToken = getIdTokenByCode(authPlatform, code);
    return parseAuthPlatformId(idToken);
  }

  @Override
  public String getIdentifier(String idToken, AuthPlatform platform) {
    return switch (platform) {
      case APPLE -> appleAuthService.getIdentifierByToken(idToken);
      case GOOGLE -> googleAuthService.getIdentifierByToken(idToken);
    };
  }

  private String getIdTokenByCode(String authPlatform, String code) {
    AuthPlatform type = AuthPlatform.find(authPlatform);

    return switch (type) {
      case APPLE -> appleAuthService.getIdTokenByCode(code).idToken();
      case GOOGLE -> googleAuthService.getIdTokenByCode(code).idToken();
    };
  }

  private String parseAuthPlatformId(String idToken) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(idToken);
      JWTClaimsSet payload = signedJWT.getJWTClaimsSet();

      return payload.getSubject();
    } catch (ParseException e) {
      throw new ClientResponseException(INVALID_ID_TOKEN);
    }
  }
}
