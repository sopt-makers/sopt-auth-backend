package sopt.makers.authentication.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3ClientConfig {

  private final ExternalProperty externalProperty;

  @Bean
  public S3Client s3Client() {
    return S3Client.builder().region(Region.of(externalProperty.s3().region())).build();
  }
}
