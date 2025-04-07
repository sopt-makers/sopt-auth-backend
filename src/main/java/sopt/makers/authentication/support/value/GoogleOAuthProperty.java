package sopt.makers.authentication.support.value;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.oauth.google")
public record GoogleOAuthProperty(Client client) {
  public record Client(String id) {}
}
