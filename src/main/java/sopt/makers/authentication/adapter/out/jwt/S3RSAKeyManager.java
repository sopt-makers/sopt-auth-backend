package sopt.makers.authentication.adapter.out.jwt;

import static sopt.makers.authentication.adapter.out.jwt.exception.ResourceFailure.*;
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

  private static final Object downloadLock = new Object();
  private static final String PUBLIC_KEY_FILE_NAME = "jwt_public_key.pem";
  private static final String PRIVATE_KEY_FILE_NAME = "jwt_private_key.pem";

  @Override
  public RSAPublicKey getPublicKey() {
    try {
      String tempFilePath = downloadPublicKeyFromS3();
      PemObject pemObject = readPemFile(tempFilePath);
      return parsePublicKey(pemObject);
    } catch (S3Exception e) {
      log.error("Failed to download public key from S3", e);
      throw e;
    }
  }

  @Override
  public RSAPrivateKey getPrivateKey() {
    try {
      String tempFilePath = downloadPrivateKeyFromS3();
      PemObject pemObject = readPemFile(tempFilePath);
      return generatePrivateKey(pemObject);
    } catch (S3Exception e) {
      log.error("Failed to download private key from S3", e);
      throw e;
    }
  }

  private String downloadPublicKeyFromS3() {
    return downloadKeyFromS3(PUBLIC_KEY_FILE_NAME, externalProperty.aws().s3().publicKeyPath());
  }

  private String downloadPrivateKeyFromS3() {
    return downloadKeyFromS3(PRIVATE_KEY_FILE_NAME, externalProperty.aws().s3().privateKeyPath());
  }

  private String downloadKeyFromS3(String fileName, String s3KeyPath) {
    synchronized (downloadLock) {
      try {
        Path baseDir = Paths.get(System.getProperty(TEMP_DIR_PROPERTY));
        Files.createDirectories(baseDir);
        String bucket = externalProperty.aws().s3().bucket();
        return s3FileManager.downloadFile(bucket, s3KeyPath, baseDir.resolve(fileName).toString());
      } catch (IOException e) {
        log.error("Failed to prepare temp directory: fileName={}", fileName, e);
        throw new ResourceException(INVALID_LOCATION);
      }
    }
  }

  private RSAPublicKey parsePublicKey(final PemObject pemObject) {
    try {
      byte[] publicKeyBytes = pemObject.getContent();
      X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(RSA);
      return (RSAPublicKey) keyFactory.generatePublic(keySpec);
    } catch (NoSuchAlgorithmException e) {
      log.error("Invalid algorithm for public key parsing", e);
      throw new ResourceException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      log.error("Invalid key spec for public key parsing", e);
      throw new ResourceException(INVALID_SUBJECT);
    }
  }

  private PemObject readPemFile(final String filePath) {
    try {
      String content = Files.readString(Paths.get(filePath), StandardCharsets.UTF_8);
      try (PemReader pemReader = new PemReader(new StringReader(content))) {
        PemObject pemObject = pemReader.readPemObject();
        boolean isPemObjectEmpty = (pemObject == null);

        if (isPemObjectEmpty) {
          throw new ResourceException(INVALID_PEM_FORMAT);
        }
        return pemObject;
      }
    } catch (IOException e) {
      log.error("Failed to read PEM file: filePath={}", filePath, e);
      throw new ResourceException(INVALID_LOCATION);
    }
  }

  private RSAPrivateKey generatePrivateKey(final PemObject pemObject) {
    try {
      byte[] privateKeyBytes = pemObject.getContent();
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(RSA);
      return (RSAPrivateKey) keyFactory.generatePrivate(keySpec);
    } catch (NoSuchAlgorithmException e) {
      log.error("Invalid algorithm for private key generation", e);
      throw new ResourceException(INVALID_ALGORITHM);
    } catch (InvalidKeySpecException e) {
      log.error("Invalid key spec for private key generation", e);
      throw new ResourceException(INVALID_SUBJECT);
    }
  }
}
