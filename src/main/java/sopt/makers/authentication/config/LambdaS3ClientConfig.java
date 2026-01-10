package sopt.makers.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@Profile({"dev", "prod"})
public class LambdaS3ClientConfig {

  @Bean
  public S3Client s3Client() {
    return S3Client.create();
  }
}
