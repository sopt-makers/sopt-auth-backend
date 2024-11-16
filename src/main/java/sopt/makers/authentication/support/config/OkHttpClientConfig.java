package sopt.makers.authentication.support.config;

import org.springframework.context.annotation.Bean;

import okhttp3.OkHttpClient;

public class OkHttpClientConfig {
  @Bean
  public OkHttpClient okHttpClient() {
    return new OkHttpClient.Builder().build();
  }
}
