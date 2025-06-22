package sopt.makers.authentication.adapter.out.external.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.oauth.google")
public record GoogleOAuthProperty(Client client) {
  public record Client(String id) {}
}
