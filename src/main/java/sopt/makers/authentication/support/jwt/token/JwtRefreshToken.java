package sopt.makers.authentication.support.jwt.token;

import sopt.makers.authentication.support.common.code.failure.TokenFailure;
import sopt.makers.authentication.support.common.exception.TokenException;
import sopt.makers.authentication.support.constant.JwtConstant;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

public class JwtRefreshToken {

  private final String token;

  public JwtRefreshToken(final String token) {
    this.token = token;
  }

  public static JwtRefreshToken createRefreshToken(JwtEncoder jwtEncoder) {
    String uuid = UUID.randomUUID().toString();
    Instant now = Instant.now();
    Instant expiration = getExpireInstant(now);

    JwtClaimsSet claimsSet =
        JwtClaimsSet.builder().claim("uuid", uuid).expiresAt(expiration).issuedAt(now).build();
    return new JwtRefreshToken(
        jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue());
  }

  public static JwtRefreshToken fromStringToken(String token) {
    return new JwtRefreshToken(token);
  }

  public String getTokenValue() {
    return token;
  }

  public void validateExpire(JwtDecoder jwtDecoder) {
    Jwt jwt = jwtDecoder.decode(token);
    Instant expiration = jwt.getExpiresAt();
    if (expiration.isBefore(Instant.now())) {
      throw new TokenException(TokenFailure.TOKEN_EXPIRED);
    }
  }

  public JwtRefreshToken refresh(JwtEncoder jwtEncoder, JwtDecoder jwtDecoder) {
    return createRefreshToken(jwtEncoder);
  }

  private static Instant getExpireInstant(Instant now) {
    return now.plusSeconds(JwtConstant.REFRESH_TOKEN_EXPIRATION);
  }
}
