package sopt.makers.authentication.support.value;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.oauth.apple")
public record AppleOAuthProperty(
    String aud, String sub, Key key, Team team, Expiration expiration) {

  public record Key(String id, String path) {}

  public record Team(String id) {}

  public record Expiration(int tokenExpiration) {}
}
