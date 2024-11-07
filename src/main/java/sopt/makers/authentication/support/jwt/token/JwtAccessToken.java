package sopt.makers.authentication.support.jwt.token;

import static sopt.makers.authentication.support.common.code.failure.TokenFailure.TOKEN_EXPIRED;
import static sopt.makers.authentication.support.common.code.failure.TokenFailure.UNSUPPORTED_ISSUER;

import sopt.makers.authentication.support.common.code.failure.TokenFailure;
import sopt.makers.authentication.support.common.exception.TokenException;
import sopt.makers.authentication.support.constant.JwtConstant;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;

import lombok.Getter;

public class JwtAccessToken {

  @Getter private final String tokenValue;
  private final Jwt jwt;

  public JwtAccessToken(final String tokenValue, final Jwt jwt) {
    this.tokenValue = tokenValue;
    this.jwt = jwt;
    validate();
  }

  public static JwtAccessToken createAccessToken(
      CustomAuthentication authentication, JwtEncoder jwtEncoder) {
    String subject = authentication.getPrincipal().toString();
    String issuer = JwtConstant.ISSUER;
    Instant now = Instant.now();
    Instant expiration = now.plusSeconds(JwtConstant.ACCESS_TOKEN_EXPIRATION);
    List<String> roles =
        authentication.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toUnmodifiableList());
    JwtClaimsSet claimsSet = generateClaimSet(subject, issuer, now, expiration, roles);
    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet));
    String token = jwtEncoder.encode(JwtEncoderParameters.from(claimsSet)).getTokenValue();
    return new JwtAccessToken(token, jwt);
  }

  public static JwtAccessToken fromStringToken(String rawToken, JwtDecoder jwtDecoder) {
    Jwt jwt = jwtDecoder.decode(rawToken);
    return new JwtAccessToken(rawToken, jwt);
  }

  private static JwtClaimsSet generateClaimSet(
      String subject, String issuer, Instant issuedAt, Instant expiresAt, List<String> roles) {
    return JwtClaimsSet.builder()
        .subject(subject)
        .issuer(issuer)
        .issuedAt(issuedAt)
        .expiresAt(expiresAt)
        .claim("roles", roles)
        .build();
  }

  public CustomAuthentication parse() {
    List<GrantedAuthority> authorities = extractAuthorities(jwt);
    return new CustomAuthentication(jwt.getSubject(), authorities);
  }

  public void validate() {
    validateExpiration();
    validateIssuer();
    validateSubject();
  }

  private List<GrantedAuthority> extractAuthorities(Jwt jwt) {
    List<String> roles = jwt.getClaim("roles");
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toUnmodifiableList());
  }

  private void validateExpiration() {
    Date expiration = Date.from(this.jwt.getExpiresAt());
    if (expiration == null || expiration.before(new Date())) {
      throw new TokenException(TOKEN_EXPIRED);
    }
  }

  private void validateIssuer() {
    String issuer = jwt.getClaim(JwtClaimNames.ISS);

    Optional.ofNullable(issuer)
        .filter(i -> Arrays.asList(JwtConstant.ISSUERS).contains(i))
        .orElseThrow(() -> new TokenException(UNSUPPORTED_ISSUER));
  }

  private void validateSubject() {
    String subject = jwt.getClaim(JwtClaimNames.SUB);
    if (subject == null) throw new TokenException(TokenFailure.INVALID_SUBJECT);
  }
}
