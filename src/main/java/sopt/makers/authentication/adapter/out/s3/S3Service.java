package sopt.makers.authentication.adapter.out.s3;

import static sopt.makers.authentication.adapter.out.external.exception.ClientError.S3_REQUEST_FAIL;
import static sopt.makers.authentication.adapter.out.external.exception.ClientError.S3_RESPONSE_UNAVAILABLE;

import sopt.makers.authentication.adapter.out.external.exception.ClientException.S3Exception;
import sopt.makers.authentication.application.port.out.s3.S3FileManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.*;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service implements S3FileManager {

  private final S3Client s3Client;

  @Override
  public String downloadFile(String bucket, String key, String localFilePath) {
    Path targetPath = Paths.get(localFilePath);

    try {
      downloadToNewFile(bucket, key, targetPath);
      return localFilePath;
    } catch (FileAlreadyExistsException e) {
      return localFilePath;
    } catch (SdkException e) {
      log.error(
          "Failed to download file from S3: bucket={}, key={}, error={}",
          bucket,
          key,
          e.getMessage(),
          e);
      throw new S3Exception(S3_REQUEST_FAIL);
    } catch (IOException e) {
      log.error(
          "IO error downloading file from S3: bucket={}, key={}, localPath={}",
          bucket,
          key,
          localFilePath,
          e);
      throw new S3Exception(S3_RESPONSE_UNAVAILABLE);
    }
  }

  /**
   * Downloads the S3 object and writes it to a new local file atomically. Uses CREATE_NEW to
   * prevent TOCTOU race conditions.
   */
  private void downloadToNewFile(String bucket, String key, Path targetPath) throws IOException {
    try (InputStream s3Stream = openS3ObjectStream(bucket, key);
        OutputStream fileStream =
            Files.newOutputStream(
                targetPath, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE)) {
      s3Stream.transferTo(fileStream);
    }
  }

  private InputStream openS3ObjectStream(String bucket, String key) {
    return s3Client.getObject(GetObjectRequest.builder().bucket(bucket).key(key).build());
  }
}
