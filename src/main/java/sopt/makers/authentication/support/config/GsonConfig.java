package sopt.makers.authentication.support.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

@Configuration
public class GsonConfig {
  @Bean
  public Gson gson() {
    return new Gson();
  }
}
