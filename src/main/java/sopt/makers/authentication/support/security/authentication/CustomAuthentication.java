package sopt.makers.authentication.support.security.authentication;

import java.util.Collection;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

public class CustomAuthentication extends UsernamePasswordAuthenticationToken {

  public CustomAuthentication(final Object principal, final Object credentials) {
    super(principal, credentials);
  }

  public CustomAuthentication(
      final Object principal,
      final Object credentials,
      final Collection<? extends GrantedAuthority> authorities) {
    super(principal, credentials, authorities);
  }
}
