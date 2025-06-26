package sopt.makers.authentication.config;

import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_ALGORITHM;
import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_LOCATION;
import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_SUBJECT;
import static sopt.makers.authentication.common.constant.SystemConstant.RSA;

import sopt.makers.authentication.adapter.out.jwt.RSAKeyManager;
import sopt.makers.authentication.adapter.out.jwt.exception.ResourceException;

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
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@EnableConfigurationProperties(SecurityProperty.class)
@RequiredArgsConstructor
@Slf4j
public class LocalRSAKeyManager implements RSAKeyManager {

  private final SecurityProperty securityProperty;
  private final ResourceLoader resourceLoader;

  @Override
  public RSAPublicKey getPublicKey() {
    try {
      Resource resource = loadPublicKeyResource();
      PemObject pemObject = readPublicPemFile(resource);
      return parsePublicKey(pemObject);
    } catch (IOException e) {
      throw new ResourceException(INVALID_LOCATION);
    } catch (NoSuchAlgorithmException e) {
      throw new ResourceException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      throw new ResourceException(INVALID_SUBJECT);
    }
  }

  @Override
  public RSAPrivateKey getPrivateKey() {
    try {
      Resource resource = loadPrivateKeyResource();
      PemObject pemObject = readPrivatePemFile(resource);
      return generatePrivateKey(pemObject);
    } catch (IOException e) {
      throw new ResourceException(INVALID_LOCATION);
    } catch (NoSuchAlgorithmException e) {
      throw new ResourceException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      throw new ResourceException(INVALID_SUBJECT);
    }
  }

  private Resource loadPublicKeyResource() {
    return resourceLoader.getResource(securityProperty.jwt().secret().rsa().publicKey());
  }

  private PemObject readPublicPemFile(final Resource resource) throws IOException {
    try (PemReader pemReader =
        new PemReader(new StringReader(resource.getContentAsString(StandardCharsets.UTF_8)))) {
      return pemReader.readPemObject();
    }
  }

  private RSAPublicKey parsePublicKey(final PemObject pemObject)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] publicKeyBytes = pemObject.getContent();
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(RSA);
    return (RSAPublicKey) keyFactory.generatePublic(keySpec);
  }

  private Resource loadPrivateKeyResource() {
    return resourceLoader.getResource(securityProperty.jwt().secret().rsa().privateKey());
  }

  private PemObject readPrivatePemFile(final Resource resource) throws IOException {
    try (PemReader pemReader =
        new PemReader(new StringReader(resource.getContentAsString(StandardCharsets.UTF_8)))) {
      return pemReader.readPemObject();
    }
  }

  private RSAPrivateKey generatePrivateKey(final PemObject pemObject)
      throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] privateKeyBytes = pemObject.getContent();
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance(RSA);
    return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
  }
}
