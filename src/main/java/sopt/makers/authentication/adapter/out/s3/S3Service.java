package sopt.makers.authentication.adapter.out.s3;

import static sopt.makers.authentication.adapter.out.external.exception.ClientError.S3_REQUEST_FAIL;
import static sopt.makers.authentication.adapter.out.external.exception.ClientError.S3_RESPONSE_UNAVAILABLE;

import sopt.makers.authentication.adapter.out.external.exception.ClientException.S3Exception;
import sopt.makers.authentication.application.port.out.s3.S3FileManager;

import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.exception.SdkException;
import software.amazon.awssdk.core.sync.ResponseTransformer;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3Service implements S3FileManager {

  private final S3Client s3Client;

  @Override
  public String downloadFile(String bucket, String key, String localFilePath) {
    try {
      log.info(
          "Downloading file from S3: bucket={}, key={}, localPath={}", bucket, key, localFilePath);
      s3Client.getObject(
          GetObjectRequest.builder().bucket(bucket).key(key).build(),
          ResponseTransformer.toFile(Paths.get(localFilePath)));
      log.info("Successfully downloaded file to: {}", localFilePath);
      return localFilePath;
    } catch (SdkException e) {
      log.error(
          "Failed to download file from S3: bucket={}, key={}, error={}",
          bucket,
          key,
          e.getMessage(),
          e);
      throw new S3Exception(S3_REQUEST_FAIL);
    } catch (Exception e) {
      log.error(
          "Unexpected error downloading file from S3: bucket={}, key={}, localPath={}, error={}",
          bucket,
          key,
          localFilePath,
          e.getMessage(),
          e);
      throw new S3Exception(S3_RESPONSE_UNAVAILABLE);
    }
  }
}
