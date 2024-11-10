package sopt.makers.authentication.support.jwt.provider;

import sopt.makers.authentication.support.jwt.JwtProvider;
import sopt.makers.authentication.support.jwt.token.JwtAccessToken;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;
import sopt.makers.authentication.support.value.JwtProperty;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthAccessTokenProvider implements JwtProvider<CustomAuthentication> {

  private final JwtTokenUtil tokenUtil;
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final JwtProperty jwtProperty;

  @Override
  public String generate(CustomAuthentication authentication) {

    String subject = authentication.getPrincipal().toString();
    String issuer = jwtProperty.secret().issuer().issuerName();
    Instant now = Instant.now();
    Instant expiration = now.plusSeconds(jwtProperty.secret().expiration().accessTokenExpiration());
    List<String> roles =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toUnmodifiableList());

    JwtClaimsSet claimsSet = generateClaimSet(subject, issuer, now, expiration, roles);
    JwtAccessToken jwtAccessToken =
        JwtAccessToken.createJwtAccessToken(
            jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)));

    jwtAccessToken.validate(jwtProperty);
    return tokenUtil.addPrefix(jwtAccessToken.getToken());
  }

  @Override
  public CustomAuthentication parse(String requestToken) throws IOException {
    String token = tokenUtil.extract(requestToken);
    Jwt accessToken = jwtDecoder.decode(token);
    JwtAccessToken jwtAccessToken = JwtAccessToken.createJwtAccessToken(accessToken);
    return jwtAccessToken.parse();
  }

  private JwtClaimsSet generateClaimSet(
      String subject, String issuer, Instant issuedAt, Instant expiresAt, List<String> roles) {
    return JwtClaimsSet.builder()
        .subject(subject)
        .issuer(issuer)
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .claim("roles", roles)
        .build();
  }
}
