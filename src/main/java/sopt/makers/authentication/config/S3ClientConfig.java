package sopt.makers.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3ClientConfig {

  private final ExternalProperty externalProperty;

  @Bean
  public S3Client s3Client() {
    return S3Client.builder()
        .region(Region.of(externalProperty.aws().region()))
        .credentialsProvider(resolveCredentialsProvider())
        .build();
  }

  private AwsCredentialsProvider resolveCredentialsProvider() {
    boolean isLocalProfile =
        StringUtils.hasText(externalProperty.aws().accessKey())
            && StringUtils.hasText(externalProperty.aws().secretKey());
    if (isLocalProfile) {
      return StaticCredentialsProvider.create(
          AwsBasicCredentials.create(
              externalProperty.aws().accessKey(), externalProperty.aws().secretKey()));
    }
    return DefaultCredentialsProvider.create();
  }
}
