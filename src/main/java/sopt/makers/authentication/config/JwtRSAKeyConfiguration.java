package sopt.makers.authentication.config;

import sopt.makers.authentication.support.jwt.RSAKeyManager;
import sopt.makers.authentication.support.value.SecurityProperty;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class JwtRSAKeyConfiguration {

  private final RSAKeyManager keyManager;
  private final SecurityProperty securityProperty;

  @Bean
  public JwtEncoder jwtEncoder() {
    RSAPublicKey publicKey = keyManager.getPublicKey();
    RSAPrivateKey privateKey = keyManager.getPrivateKey();
    String keyId = securityProperty.jwt().secret().rsa().keyId();
    JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID(keyId).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(keyManager.getPublicKey()).build();
  }
}
