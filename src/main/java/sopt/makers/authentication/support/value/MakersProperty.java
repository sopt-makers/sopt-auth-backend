package sopt.makers.authentication.support.value;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.makers")
public record MakersProperty(Playground playground) {
  public record Playground(String url, String token) {}
}
