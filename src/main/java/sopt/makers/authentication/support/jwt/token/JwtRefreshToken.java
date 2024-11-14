package sopt.makers.authentication.support.jwt.token;

import static sopt.makers.authentication.support.common.code.failure.TokenFailure.TOKEN_EXPIRED;

import sopt.makers.authentication.support.common.exception.TokenException;

import java.time.Instant;

import org.springframework.security.oauth2.jwt.Jwt;

public class JwtRefreshToken {

  private final Jwt jwt;

  private JwtRefreshToken(final Jwt jwt) {
    this.jwt = jwt;
  }

  public static JwtRefreshToken createRefreshToken(Jwt jwt) {
    return new JwtRefreshToken(jwt);
  }

  public String getToken() {
    return this.jwt.getTokenValue();
  }

  public void validateExpire() {
    Instant expiration = jwt.getExpiresAt();
    boolean isTokenExpired = expiration.isBefore(Instant.now());

    if (isTokenExpired) {
      throw new TokenException(TOKEN_EXPIRED);
    }
  }
}
