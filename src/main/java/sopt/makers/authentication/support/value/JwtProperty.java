package sopt.makers.authentication.support.value;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperty(Secret secret) {

  @ConfigurationProperties(prefix = "jwt.secret")
  public record Secret(Rsa rsa, Expiration expiration, Issuer issuer) {

    @ConfigurationProperties(prefix = "jwt.secret.rsa")
    public record Rsa(String publicKey, String privateKey) {}

    @ConfigurationProperties(prefix = "jwt.secret.expiration")
    public record Expiration(long accessTokenExpiration, long refreshTokenExpiration) {}

    @ConfigurationProperties(prefix = "jwt.secret.issuer")
    public record Issuer(String issuerName) {}
  }
}
