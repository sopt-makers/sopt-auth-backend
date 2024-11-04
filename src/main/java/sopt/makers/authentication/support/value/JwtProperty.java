package sopt.makers.authentication.support.value;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperty(Secret secret) {
  public record Secret(Rsa rsa) {
    public record Rsa(String publicKey, String privateKey) {}
  }
}
