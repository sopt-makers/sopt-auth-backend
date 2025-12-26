package sopt.makers.authentication.config;

import sopt.makers.authentication.adapter.out.external.s3.S3Property;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

@Configuration
@RequiredArgsConstructor
public class S3ClientConfig {

  private final S3Property s3Property;

  @Bean
  public S3Client s3Client() {
    return S3Client.builder().region(Region.of(s3Property.region())).build();
  }
}
