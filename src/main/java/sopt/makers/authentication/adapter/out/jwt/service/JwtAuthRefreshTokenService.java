package sopt.makers.authentication.adapter.out.jwt.service;

import sopt.makers.authentication.adapter.out.jwt.JwtProvider;
import sopt.makers.authentication.adapter.out.jwt.token.JwtRefreshToken;
import sopt.makers.authentication.config.SecurityProperty;

import java.time.Instant;
import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthRefreshTokenService implements JwtProvider<String> {

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final SecurityProperty securityProperty;

  @Override
  public String generateJwt(String accessToken) {
    JwtClaimsSet claimsSet = generateClaimSet();
    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet));
    JwtRefreshToken jwtRefreshToken = JwtRefreshToken.createRefreshToken(jwt);

    return jwtRefreshToken.getToken();
  }

  @Override
  public String parse(String requestToken) {
    Jwt jwt = jwtDecoder.decode(requestToken);

    JwtRefreshToken jwtRefreshToken = JwtRefreshToken.createRefreshToken(jwt);
    jwtRefreshToken.validateExpire();
    return jwtRefreshToken.getToken();
  }

  private JwtClaimsSet generateClaimSet() {
    String id = UUID.randomUUID().toString();
    Instant issueDate = Instant.now();
    Instant expirationDate =
        issueDate.plusSeconds(
            securityProperty.jwt().secret().expiration().refreshTokenExpiration());

    return JwtClaimsSet.builder()
        .id(id)
        .issuer(securityProperty.jwt().secret().issuer().issuerName())
        .issuedAt(issueDate)
        .expiresAt(expirationDate)
        .build();
  }
}
