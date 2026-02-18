package sopt.makers.authentication.application.port.out.s3;

public interface S3FileManager {
  String downloadFile(String bucket, String key, String localFilePath);
}
