package sopt.makers.authentication.support.jwt.provider;

import static sopt.makers.authentication.support.jwt.provider.JwtTokenUtil.extract;

import sopt.makers.authentication.support.jwt.JwtProvider;
import sopt.makers.authentication.support.jwt.token.JwtAccessToken;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;
import sopt.makers.authentication.support.value.SecurityProperty;

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
  private static final String ROLES = "roles";
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;
  private final SecurityProperty securityProperty;

  @Override
  public String generateJwtAuthToken(CustomAuthentication authentication) {
    String subject = authentication.getPrincipal().toString();
    String issuer = securityProperty.jwt().secret().issuer().issuerName();
    Instant now = Instant.now();
    Instant expiration = calculateExpiration(now);
    List<String> roles = extractRoles(authentication);
    JwtClaimsSet claimsSet = generateClaimsSet(subject, issuer, now, expiration, roles);
    JwtAccessToken jwtAccessToken = generateJwtAccessToken(claimsSet);

    jwtAccessToken.validate(securityProperty);
    return jwtAccessToken.getToken();
  }

  private Instant calculateExpiration(Instant now) {
    long seconds = securityProperty.jwt().secret().expiration().accessTokenExpiration();
    return now.plusSeconds(seconds);
  }

  private List<String> extractRoles(CustomAuthentication authentication) {
    return authentication.getAuthorities().stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toUnmodifiableList());
  }

  private JwtClaimsSet generateClaimsSet(
      String subject, String issuer, Instant issuedAt, Instant expiresAt, List<String> roles) {
    return JwtClaimsSet.builder()
        .subject(subject)
        .issuer(issuer)
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .claim(ROLES, roles)
        .build();
  }

  private JwtAccessToken generateJwtAccessToken(JwtClaimsSet claims) {
    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims));
    return JwtAccessToken.createJwtAccessToken(jwt);
  }

  @Override
  public CustomAuthentication parse(String requestToken) {
    String token = extract(requestToken);
    Jwt accessToken = jwtDecoder.decode(token);
    JwtAccessToken jwtAccessToken = JwtAccessToken.createJwtAccessToken(accessToken);
    return jwtAccessToken.parse();
  }
}
