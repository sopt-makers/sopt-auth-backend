package sopt.makers.authentication.application.port.out.s3;

import java.io.IOException;

public interface S3FileManager {
  String downloadFile(String bucket, String key, String localFilePath) throws IOException;
}
