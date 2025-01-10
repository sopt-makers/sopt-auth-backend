package sopt.makers.authentication.external.oauth;

import static sopt.makers.authentication.support.code.external.failure.ClientError.*;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_ALGORITHM_HEADER;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_ALGORITHM_VALUE;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_ISSUER;
import static sopt.makers.authentication.support.constant.OAuthConstant.APPLE_KEY_ID_HEADER;

import sopt.makers.authentication.external.oauth.client.AppleAuthClient;
import sopt.makers.authentication.external.oauth.dto.IdTokenResponse;
import sopt.makers.authentication.support.code.domain.failure.AuthFailure;
import sopt.makers.authentication.support.code.support.failure.TokenFailure;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.support.exception.external.ClientRequestException;
import sopt.makers.authentication.support.exception.support.TokenException;
import sopt.makers.authentication.support.util.*;
import sopt.makers.authentication.support.value.AppleOAuthProperty;

import java.security.PrivateKey;
import java.text.ParseException;
import java.time.Instant;
import java.util.Date;

import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.ECDSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class AppleAuthService implements OAuthService {
  private final AppleOAuthProperty appleOAuthProperty;
  private final AppleAuthClient appleAuthClient;

  @Override
  public IdTokenResponse getIdTokenByCode(final String code) {
    String clientSecret = createClientSecret();
    return appleAuthClient.getIdToken(clientSecret, code);
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
    JWKSet loadedJWKSet = appleAuthClient.getPublicKeySet();
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
      boolean isCorrectIssuer = jwtClaimsSet.getIssuer().equals(APPLE_ISSUER);
      boolean isCorrectAudience = jwtClaimsSet.getAudience().contains(appleOAuthProperty.aud());
      boolean isNotExpired = jwtClaimsSet.getExpirationTime().after(Date.from(Instant.now()));

      if (!(isVerifiedSignature && isCorrectIssuer && isCorrectAudience && isNotExpired)) {
        throw new AuthException(AuthFailure.INVALID_ID_TOKEN);
      }
    } catch (JOSEException e) {
      throw new AuthException(AuthFailure.INVALID_ID_TOKEN);
    }
  }

  // TODO : AuthXXX 객체에서 ClientXXXException 발생하는 구조는 개선되면 좋을 것 같습니다. (@동규)
  private String createClientSecret() {
    Date now = new Date();
    PrivateKey privateKey =
        KeyFileUtil.getPrivateKey(appleOAuthProperty.key().path())
            .orElseThrow(() -> new ClientRequestException(FAIL_READ_APPLE_PRIVATE_KEY_FILE));

    return Jwts.builder() // 토큰 생성 로직은 tokenProvider? 근데 얘는 parse는 없음
        .setHeaderParam(APPLE_KEY_ID_HEADER, appleOAuthProperty.key().id())
        .setHeaderParam(APPLE_ALGORITHM_HEADER, APPLE_ALGORITHM_VALUE)
        .setIssuedAt(now)
        .setExpiration(new Date(now.getTime() + appleOAuthProperty.expiration().tokenExpiration()))
        .setIssuer(appleOAuthProperty.team().id())
        .setAudience(appleOAuthProperty.aud())
        .setSubject(appleOAuthProperty.sub())
        .signWith(privateKey, SignatureAlgorithm.ES256)
        .compact();
  }
}
