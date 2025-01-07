package sopt.makers.authentication.external.oauth;

import static sopt.makers.authentication.support.code.external.failure.ClientError.*;
import static sopt.makers.authentication.support.constant.OAuthConstant.*;

import sopt.makers.authentication.external.oauth.client.GoogleAuthClient;
import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.code.domain.failure.AuthFailure;
import sopt.makers.authentication.support.code.support.failure.TokenFailure;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.support.exception.support.TokenException;
import sopt.makers.authentication.support.value.GoogleOAuthProperty;

import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.RequiredArgsConstructor;
import okhttp3.OkHttpClient;

@Component
@RequiredArgsConstructor
public class GoogleAuthService implements OAuthService {
  private final GoogleOAuthProperty googleOAuthProperty;
  private final GoogleAuthClient googleAuthClient;
  private final Gson gson;
  private final OkHttpClient client;

  @Override
  public IdTokenResponse getIdTokenByCode(String code) {
    return googleAuthClient.getIdToken(googleOAuthProperty.client().secret(), code);
  }

  public String getIdentifierByToken(final String token) {
    try {
      SignedJWT signedJWT = SignedJWT.parse(token);
      JWK targetJwk = findMatchJWK(signedJWT);

      verifyAppleIdTokenJwt(signedJWT, targetJwk);
      String identifier = signedJWT.getJWTClaimsSet().getSubject();
      return identifier;
    } catch (ParseException e) {
      throw new TokenException(TokenFailure.TOKEN_PARSE_FAILED);
    }
  }

  private JWK findMatchJWK(final SignedJWT jwt) {
    JWKSet loadedJWKSet = googleAuthClient.getPublicKeySet();
    String keyID = jwt.getHeader().getKeyID();
    return loadedJWKSet.getKeys().stream()
        .filter(jwk -> jwk.getKeyID().equals(keyID))
        .findFirst()
        .orElseThrow(() -> new AuthException(AuthFailure.NOT_FOUND_AVAILABLE_PUBLIC_KEY_SET));
  }

  private void verifyAppleIdTokenJwt(final SignedJWT jwt, JWK jwk) throws ParseException {
    try {
      JWTClaimsSet jwtClaimsSet = jwt.getJWTClaimsSet();
      JWSVerifier verifier = new ECDSAVerifier(jwk.toECKey());

      boolean isVerifiedSignature = jwt.verify(verifier);
      boolean isCorrectIssuer = jwtClaimsSet.getIssuer().equals(GOOGLE_ISSUER);
      boolean isCorrectAudience =
          jwtClaimsSet.getAudience().contains(googleOAuthProperty.client().id());
      boolean isNotExpired = jwtClaimsSet.getExpirationTime().after(Date.from(Instant.now()));

      if (!(isVerifiedSignature && isCorrectIssuer && isCorrectAudience && isNotExpired)) {
        throw new AuthException(AuthFailure.INVALID_ID_TOKEN);
      }
    } catch (JOSEException e) {
      throw new AuthException(AuthFailure.INVALID_ID_TOKEN);
    }
  }
}
