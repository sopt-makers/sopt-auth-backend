package sopt.makers.authentication.adapter.out.external.oauth;

import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_ISSUER;

import sopt.makers.authentication.adapter.out.external.oauth.client.AppleAuthClient;
import sopt.makers.authentication.adapter.out.jwt.exception.TokenException;
import sopt.makers.authentication.adapter.out.jwt.exception.TokenFailure;
import sopt.makers.authentication.domain.auth.exception.AuthException;
import sopt.makers.authentication.domain.auth.exception.AuthFailure;
import sopt.makers.authentication.support.value.AppleOAuthProperty;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleAuthService implements OAuthService {
  private final AppleOAuthProperty appleOAuthProperty;
  private final AppleAuthClient appleAuthClient;

  @Override
  public String getIdentifierByToken(final String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWK targetJwk = findMatchJWK(signedJWT);

      verifyAppleIdTokenJwt(signedJWT, targetJwk);
      return signedJWT.getJWTClaimsSet().getSubject();
    } catch (ParseException e) {
      throw new TokenException(TokenFailure.TOKEN_PARSE_FAILED);
    }
  }

  private JWK findMatchJWK(final SignedJWT jwt) {
    JWKSet loadedJWKSet = appleAuthClient.getPublicKeySet();
    String kid = jwt.getHeader().getKeyID();
    String alg = jwt.getHeader().getAlgorithm().getName();

    return loadedJWKSet.getKeys().stream()
        .filter(jwk -> jwk.getKeyID().equals(kid) && jwk.getAlgorithm().getName().equals(alg))
        .findFirst()
        .orElseThrow(() -> new AuthException(AuthFailure.NOT_FOUND_AVAILABLE_PUBLIC_KEY_SET));
  }

  private void verifyAppleIdTokenJwt(final SignedJWT jwt, JWK jwk) throws ParseException {
    try {
      JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
      JWSVerifier verifier = new RSASSAVerifier(jwk.toRSAKey());

      boolean isVerifiedSignature = jwt.verify(verifier);
      boolean isCorrectIssuer = jwtClaimsSet.getIssuer().equals(APPLE_ISSUER);
      boolean isCorrectAudience = verifyAudience(jwtClaimsSet.getAudience());
      boolean isNotExpired = jwtClaimsSet.getExpirationTime().after(Date.from(Instant.now()));

      if (!(isVerifiedSignature && isCorrectIssuer && isCorrectAudience && isNotExpired)) {
        throw new AuthException(AuthFailure.INVALID_ID_TOKEN);
      }
    } catch (JOSEException e) {
      throw new AuthException(AuthFailure.INVALID_ID_TOKEN);
    }
  }

  private boolean verifyAudience(List<String> audiences) {
    return audiences.contains(appleOAuthProperty.webAud())
        || audiences.contains(appleOAuthProperty.appAud());
  }
}
