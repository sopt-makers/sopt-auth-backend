package sopt.makers.authentication.adapter.out.jwt;

import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_ALGORITHM;
import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_LOCATION;
import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_SUBJECT;
import static sopt.makers.authentication.common.constant.SystemConstant.RSA;

import sopt.makers.authentication.adapter.out.jwt.exception.ResourceException;
import sopt.makers.authentication.application.port.out.auth.RSAKeyManager;
import sopt.makers.authentication.application.port.out.s3.S3FileManager;
import sopt.makers.authentication.config.ExternalProperty;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class S3RSAKeyManager implements RSAKeyManager {

  private final S3FileManager s3FileManager;
  private final ExternalProperty externalProperty;
  private static final String PUBLIC_KEY_TEMP_PATH = "/tmp/jwt_public_key.pem";
  private static final String PRIVATE_KEY_TEMP_PATH = "/tmp/jwt_private_key.pem";

  @Override
  public RSAPublicKey getPublicKey() {
    try {
      String tempFilePath = downloadPublicKeyFromS3();
      PemObject pemObject = readPublicPemFile(tempFilePath);
      return parsePublicKey(pemObject);
    } catch (IOException e) {
      log.error("Failed to read public key from S3", e);
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
      String tempFilePath = downloadPrivateKeyFromS3();
      PemObject pemObject = readPrivatePemFile(tempFilePath);
      return generatePrivateKey(pemObject);
    } catch (IOException e) {
      log.error("Failed to read private key from S3", e);
      throw new ResourceException(INVALID_LOCATION);
    } catch (NoSuchAlgorithmException e) {
      throw new ResourceException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      throw new ResourceException(INVALID_SUBJECT);
    }
  }

  private String downloadPublicKeyFromS3() throws IOException {
    String bucket = externalProperty.s3().jwt().bucket();
    String key = externalProperty.s3().jwt().publicKeyPath();

    return s3FileManager.downloadFile(bucket, key, PUBLIC_KEY_TEMP_PATH);
  }

  private String downloadPrivateKeyFromS3() throws IOException {
    String bucket = externalProperty.s3().jwt().bucket();
    String key = externalProperty.s3().jwt().privateKeyPath();

    return s3FileManager.downloadFile(bucket, key, PRIVATE_KEY_TEMP_PATH);
  }

  private PemObject readPublicPemFile(final String filePath) throws IOException {
    String content = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
    try (PemReader pemReader = new PemReader(new StringReader(content))) {
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

  private PemObject readPrivatePemFile(final String filePath) throws IOException {
    String content = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
    try (PemReader pemReader = new PemReader(new StringReader(content))) {
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
