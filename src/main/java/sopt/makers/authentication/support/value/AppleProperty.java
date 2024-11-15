package sopt.makers.authentication.support.value;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.oauth.apple")
public record AppleProperty(Apple apple) {
  public record Apple(String aud, String sub, Key key, Team team) {}

  public record Key(String id, String path) {}

  public record Team(String id) {}
}
