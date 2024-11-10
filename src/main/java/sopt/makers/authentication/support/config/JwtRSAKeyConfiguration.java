package sopt.makers.authentication.support.config;

import static sopt.makers.authentication.support.common.code.failure.TokenFailure.INVALID_ALGORITHM;
import static sopt.makers.authentication.support.common.code.failure.TokenFailure.INVALID_LOCATION;
import static sopt.makers.authentication.support.common.code.failure.TokenFailure.INVALID_SUBJECT;

import sopt.makers.authentication.support.common.exception.TokenException;
import sopt.makers.authentication.support.value.JwtProperty;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
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
import com.nimbusds.jose.util.StandardCharset;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties(JwtProperty.class)
@Slf4j
public class JwtRSAKeyConfiguration {

  private final JwtProperty jwtProperty;
  private final ResourceLoader resourceLoader;

  public JwtRSAKeyConfiguration(JwtProperty jwtProperty, ResourceLoader resourceLoader) {
    this.jwtProperty = jwtProperty;
    this.resourceLoader = resourceLoader;
  }

  @Bean
  public RSAPublicKey createPublicKeyFromProperty() {
    Resource resource = resourceLoader.getResource(jwtProperty.secret().rsa().publicKey());
    try {
      PemReader pemReader =
          new PemReader(new StringReader(resource.getContentAsString(StandardCharset.UTF_8)));
      PemObject pemObject = pemReader.readPemObject();
      pemReader.close();

      byte[] publicKeyBytes = pemObject.getContent();
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    } catch (IOException e) {
      throw new TokenException(INVALID_LOCATION);
    } catch (NoSuchAlgorithmException e) {
      throw new TokenException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      throw new TokenException(INVALID_SUBJECT);
    }
  }

  @Bean
  public RSAPrivateKey createPrivateKeyFromProperty() {
    Resource resource = resourceLoader.getResource(jwtProperty.secret().rsa().privateKey());
    try {
      PemReader pemReader =
          new PemReader(new StringReader(resource.getContentAsString(StandardCharsets.UTF_8)));
      PemObject pemObject = pemReader.readPemObject();
      pemReader.close();

      byte[] privateKeyBytes = pemObject.getContent();
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);

      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    } catch (IOException e) {
      throw new TokenException(INVALID_LOCATION);
    } catch (NoSuchAlgorithmException e) {
      throw new TokenException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      throw new TokenException(INVALID_SUBJECT);
    }
  }

  @Bean
  public JwtEncoder jwtEncoder() {
    RSAPublicKey publicKey = createPublicKeyFromProperty();
    RSAPrivateKey privateKey = createPrivateKeyFromProperty();

    JWK jwk = new RSAKey.Builder(publicKey).privateKey(privateKey).build();
    JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
    return new NimbusJwtEncoder(jwks);
  }

  @Bean
  public JwtDecoder jwtDecoder() {
    return NimbusJwtDecoder.withPublicKey(createPublicKeyFromProperty()).build();
  }
}
