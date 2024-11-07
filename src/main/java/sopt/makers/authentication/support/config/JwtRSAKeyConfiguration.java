package sopt.makers.authentication.support.config;

import sopt.makers.authentication.support.value.JwtProperty;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
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

@Configuration
@EnableConfigurationProperties(JwtProperty.class)
public class JwtRSAKeyConfiguration {

  private final JwtProperty jwtProperty;

  public JwtRSAKeyConfiguration(JwtProperty jwtProperty) {
    this.jwtProperty = jwtProperty;
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    JWK jwk =
        new RSAKey.Builder(jwtProperty.publicKey()).privateKey(jwtProperty.privateKey()).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(jwtProperty.publicKey()).build();
  }
}
