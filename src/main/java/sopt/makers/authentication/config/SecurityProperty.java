package sopt.makers.authentication.config;

import java.util.List;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperty(Api api, Jwt jwt) {

  public record Api(Map<String, String> keys, List<String> securedEndpoints) {}

  public record Jwt(Secret secret) {

    public record Secret(Rsa rsa, Expiration expiration, Issuer issuer) {

      public record Rsa(String keyId, String publicKey, String privateKey) {}

      public record Expiration(long accessTokenExpiration, long refreshTokenExpiration) {}

      public record Issuer(String issuerName) {}
    }
  }
}
