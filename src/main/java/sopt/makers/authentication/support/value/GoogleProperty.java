package sopt.makers.authentication.support.value;

import java.lang.ProcessBuilder.*;

import org.springframework.boot.context.properties.*;

@ConfigurationProperties(prefix = "external.oauth.google")
public record GoogleProperty(Google google) {
  public record Google(Redirect redirect, Client client) {}

  public record Redirect(String url) {}

  public record Client(String id, String secret) {}
}
