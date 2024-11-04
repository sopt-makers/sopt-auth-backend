package sopt.makers.authentication.support.value;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth")
public record AuthProperty(Client client) {
  public record Client(String url) {}
}
