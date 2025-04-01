package sopt.makers.authentication.support.value;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "security")
public record SecurityProperty(Api api, Jwt jwt) {

  @ConfigurationProperties(prefix = "security.api")
  public record Api(String key, List<String> securedEndpoints) {}

  @ConfigurationProperties(prefix = "security.jwt")
  public record Jwt(Secret secret) {

    @ConfigurationProperties(prefix = "security.jwt.secret")
    public record Secret(Rsa rsa, Expiration expiration, Issuer issuer) {

      @ConfigurationProperties(prefix = "security.jwt.secret.rsa")
      public record Rsa(String publicKey, String privateKey) {}

      @ConfigurationProperties(prefix = "security.jwt.secret.expiration")
      public record Expiration(long accessTokenExpiration, long refreshTokenExpiration) {}

      @ConfigurationProperties(prefix = "security.jwt.secret.issuer")
      public record Issuer(String issuerName) {}
    }
  }
}
