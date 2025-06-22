package sopt.makers.authentication.support.security.authentication;

import static sopt.makers.authentication.common.constant.SystemConstant.INTERNAL_SERVICE;
import static sopt.makers.authentication.common.constant.SystemConstant.ROLE;

import java.util.List;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class ApiKeyAuthentication extends AbstractAuthenticationToken {
  private final String apiKey;
  private final String serviceName;

  public ApiKeyAuthentication(String apiKey, String serviceName) {
    super(List.of(new SimpleGrantedAuthority(ROLE + INTERNAL_SERVICE)));
    this.apiKey = apiKey;
    this.serviceName = serviceName;
    super.setAuthenticated(true);
  }

  @Override
  public Object getCredentials() {
    return apiKey;
  }

  @Override
  public Object getPrincipal() {
    return serviceName;
  }
}
