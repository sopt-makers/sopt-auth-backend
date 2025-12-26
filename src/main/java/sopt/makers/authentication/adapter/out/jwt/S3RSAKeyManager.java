package sopt.makers.authentication.adapter.out.jwt;

import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_ALGORITHM;
import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_LOCATION;
import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.INVALID_SUBJECT;
import static sopt.makers.authentication.common.constant.SystemConstant.RSA;
import static sopt.makers.authentication.common.constant.SystemConstant.TEMP_DIR_PROPERTY;

import sopt.makers.authentication.adapter.out.external.exception.ClientException.S3Exception;
import sopt.makers.authentication.adapter.out.jwt.exception.ResourceException;
import sopt.makers.authentication.application.port.out.auth.RSAKeyManager;
import sopt.makers.authentication.application.port.out.s3.S3FileManager;
import sopt.makers.authentication.config.ExternalProperty;

import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
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

  private static final String PUBLIC_KEY_FILE_NAME = "jwt_public_key.pem";
  private static final String PRIVATE_KEY_FILE_NAME = "jwt_private_key.pem";

  @Override
  public RSAPublicKey getPublicKey() {
    try {
      String tempFilePath = downloadPublicKeyFromS3();
      PemObject pemObject = readPublicPemFile(tempFilePath);
      return parsePublicKey(pemObject);
    } catch (S3Exception e) {
      log.error("Failed to download public key from S3", e);
      throw e;
    } catch (IOException e) {
      log.error("Failed to read public key file", e);
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
    } catch (S3Exception e) {
      log.error("Failed to download private key from S3", e);
      throw e;
    } catch (IOException e) {
      log.error("Failed to read private key file", e);
      throw new ResourceException(INVALID_LOCATION);
    } catch (NoSuchAlgorithmException e) {
      throw new ResourceException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      throw new ResourceException(INVALID_SUBJECT);
    }
  }

  private String downloadPublicKeyFromS3() throws IOException {
    Path baseDir = Paths.get(System.getProperty(TEMP_DIR_PROPERTY));
    Path targetPath = baseDir.resolve(PUBLIC_KEY_FILE_NAME);

    Files.createDirectories(baseDir);

    if (Files.exists(targetPath)) {
      return targetPath.toString();
    }

    String bucket = externalProperty.aws().s3().bucket();
    String key = externalProperty.aws().s3().publicKeyPath();
    return s3FileManager.downloadFile(bucket, key, targetPath.toString());
  }

  private String downloadPrivateKeyFromS3() throws IOException {
    Path baseDir = Paths.get(System.getProperty(TEMP_DIR_PROPERTY));
    Path targetPath = baseDir.resolve(PRIVATE_KEY_FILE_NAME);

    Files.createDirectories(baseDir);
    if (Files.exists(targetPath)) {
      return targetPath.toString();
    }

    String bucket = externalProperty.aws().s3().bucket();
    String key = externalProperty.aws().s3().privateKeyPath();
    return s3FileManager.downloadFile(bucket, key, targetPath.toString());
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
