package sopt.makers.authentication.external.oauth;

import static sopt.makers.authentication.support.code.external.failure.ClientError.INVALID_ID_TOKEN;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.exception.external.*;
import sopt.makers.authentication.usecase.auth.port.out.OAuthPlatformPort;

import java.text.ParseException;

import org.springframework.stereotype.Component;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class OAuthPlatformService implements OAuthPlatformPort {
  private final AppleAuthProvider appleAuthProvider;
  private final GoogleAuthProvider googleAuthProvider;

  public String getAuthPlatformId(AuthPlatform authPlatform, String code) {
    IdTokenResponse idTokenResponse = getIdTokenByCode(authPlatform, code);
    return parseAuthPlatformId(idTokenResponse);
  }

  public IdTokenResponse getIdTokenByCode(AuthPlatform type, String code) {
    return switch (type) {
      case APPLE -> appleAuthProvider.getIdTokenByCode(code);
      case GOOGLE -> googleAuthProvider.getIdTokenByCode(code);
    };
  }

  private String parseAuthPlatformId(IdTokenResponse tokenResponse) {
    String idToken = tokenResponse.idToken();
    try {
      SignedJWT signedJWT = SignedJWT.parse(idToken);
      JWTClaimsSet payload = signedJWT.getJWTClaimsSet();

      return payload.getSubject();
    } catch (ParseException e) {
      throw new ClientResponseException(INVALID_ID_TOKEN);
    }
  }
}
