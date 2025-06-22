package sopt.makers.authentication.adapter.out.jwt.token;

import static org.springframework.security.oauth2.jwt.JwtClaimNames.ISS;
import static org.springframework.security.oauth2.jwt.JwtClaimNames.SUB;
import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_SUBJECT;
import static sopt.makers.authentication.adapter.out.jwt.exception.TokenFailure.TOKEN_EXPIRED;
import static sopt.makers.authentication.adapter.out.jwt.exception.TokenFailure.UNSUPPORTED_ISSUER;

import sopt.makers.authentication.adapter.out.jwt.exception.ResourceException;
import sopt.makers.authentication.adapter.out.jwt.exception.TokenException;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;
import sopt.makers.authentication.support.value.SecurityProperty;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

public class JwtAccessToken {

  public static final String ROLES = "roles";

  private final Jwt jwt;

  private JwtAccessToken(final Jwt jwt) {
    this.jwt = jwt;
  }

  public String getToken() {
    return this.jwt.getTokenValue();
  }

  public static JwtAccessToken createJwtAccessToken(Jwt jwt) {
    return new JwtAccessToken(jwt);
  }

  public CustomAuthentication parse() {
    List<GrantedAuthority> authorities = extractAuthorities(jwt);
    return new CustomAuthentication(jwt.getSubject(), authorities);
  }

  public void validate(SecurityProperty jwtProperty) {
    validateExpiration();
    validateIssuer(jwtProperty);
    validateSubject();
  }

  private List<GrantedAuthority> extractAuthorities(Jwt jwt) {
    List<String> roles = jwt.getClaim(ROLES);
    return roles.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toUnmodifiableList());
  }

  private void validateExpiration() {
    Instant expirationDate = jwt.getExpiresAt();
    boolean isTokenExpired = expirationDate.isBefore(Instant.now());

    if (isTokenExpired) {
      throw new TokenException(TOKEN_EXPIRED);
    }
  }

  private void validateIssuer(SecurityProperty securityProperty) {
    String issuerFromJwt = jwt.getClaim(ISS);
    boolean unsupportedIssuer =
        !issuerFromJwt.equals(securityProperty.jwt().secret().issuer().issuerName());

    if (unsupportedIssuer) {
      throw new TokenException(UNSUPPORTED_ISSUER);
    }
  }

  private void validateSubject() {
    String subject = jwt.getClaim(SUB);
    if (subject == null) {
      throw new ResourceException(INVALID_SUBJECT);
    }
  }
}
