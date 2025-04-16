package sopt.makers.authentication.support.jwt.provider;

import static sopt.makers.authentication.support.jwt.provider.JwtTokenUtil.addPrefix;
import static sopt.makers.authentication.support.jwt.provider.JwtTokenUtil.extract;

import sopt.makers.authentication.support.jwt.JwtProvider;
import sopt.makers.authentication.support.jwt.token.JwtRefreshToken;
import sopt.makers.authentication.support.value.SecurityProperty.Jwt.Secret.Expiration;
import sopt.makers.authentication.support.value.SecurityProperty.Jwt.Secret.Issuer;

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
public class JwtAuthRefreshTokenProvider implements JwtProvider<String> {

  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final Expiration tokenExpiration;
  private final Issuer issuer;

  @Override
  public String generate(String accessToken) {
    JwtClaimsSet claimsSet = generateClaimSet();
    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet));
    JwtRefreshToken jwtRefreshToken = JwtRefreshToken.createRefreshToken(jwt);

    return jwtRefreshToken.getToken();
  }

  @Override
  public String parse(String requestToken) {
    String token = extract(requestToken);
    Jwt jwt = jwtDecoder.decode(token);

    JwtRefreshToken jwtRefreshToken = JwtRefreshToken.createRefreshToken(jwt);
    jwtRefreshToken.validateExpire();
    JwtRefreshToken refreshedToken = refresh();

    return addPrefix(refreshedToken.getToken());
  }

  private JwtRefreshToken refresh() {
    JwtClaimsSet claimsSet = generateClaimSet();
    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet));
    return JwtRefreshToken.createRefreshToken(jwt);
  }

  private JwtClaimsSet generateClaimSet() {
    String id = UUID.randomUUID().toString();
    Instant issueDate = Instant.now();
    Instant expirationDate = issueDate.plusSeconds(tokenExpiration.refreshTokenExpiration());

    return JwtClaimsSet.builder()
        .id(id)
        .issuer(issuer.issuerName())
        .issuedAt(issueDate)
        .expiresAt(expirationDate)
        .build();
  }
}
