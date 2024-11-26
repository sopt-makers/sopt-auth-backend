package sopt.makers.authentication.support.config;

import static sopt.makers.authentication.support.code.support.failure.TokenFailure.INVALID_ALGORITHM;
import static sopt.makers.authentication.support.code.support.failure.TokenFailure.INVALID_LOCATION;
import static sopt.makers.authentication.support.code.support.failure.TokenFailure.INVALID_SUBJECT;

import sopt.makers.authentication.support.exception.support.TokenException;
import sopt.makers.authentication.support.jwt.RSAKeyManager;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableConfigurationProperties(JwtProperty.class)
@RequiredArgsConstructor
@Slf4j
public class LocalRSARSAKeyManager implements RSAKeyManager {

  private final JwtProperty jwtProperty;
  private final ResourceLoader resourceLoader;

  @Override
  public RSAPublicKey getPublicKey() {
    try {
      Resource resource = loadPublicKeyResource();
      PemObject pemObject = readPublicPemFile(resource);
      return parsePublicKey(pemObject);
    } catch (IOException e) {
      throw new TokenException(INVALID_LOCATION);
    } catch (NoSuchAlgorithmException e) {
      throw new TokenException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      throw new TokenException(INVALID_SUBJECT);
    }
  }

  @Override
  public RSAPrivateKey getPrivateKey() {
    try {
      Resource resource = loadPrivateKeyResource();
      PemObject pemObject = readPrivatePemFile(resource);
      return generatePrivateKey(pemObject);
    } catch (IOException e) {
      throw new TokenException(INVALID_LOCATION);
    } catch (NoSuchAlgorithmException e) {
      throw new TokenException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      throw new TokenException(INVALID_SUBJECT);
    }
  }

  private Resource loadPublicKeyResource() {
    return resourceLoader.getResource(jwtProperty.secret().rsa().publicKey());
  }

  private PemObject readPublicPemFile(Resource resource) throws IOException {
    try (PemReader pemReader =
        new PemReader(new StringReader(resource.getContentAsString(StandardCharsets.UTF_8)))) {
      return pemReader.readPemObject();
    }
  }

  private RSAPublicKey parsePublicKey(PemObject pemObject)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] publicKeyBytes = pemObject.getContent();
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPublicKey) keyFactory.generatePublic(keySpec);
  }

  private Resource loadPrivateKeyResource() {
    return resourceLoader.getResource(jwtProperty.secret().rsa().privateKey());
  }

  private PemObject readPrivatePemFile(Resource resource) throws IOException {
    try (PemReader pemReader =
        new PemReader(new StringReader(resource.getContentAsString(StandardCharsets.UTF_8)))) {
      return pemReader.readPemObject();
    }
  }

  private RSAPrivateKey generatePrivateKey(PemObject pemObject)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] privateKeyBytes = pemObject.getContent();
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
  }
}
