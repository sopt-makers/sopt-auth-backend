package sopt.makers.authentication.config;

import static sopt.makers.authentication.common.constant.CookieConstant.CORS_ALLOWED_ORIGINS;
import static sopt.makers.authentication.common.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.common.constant.SystemConstant.PATTERN_ALL;
import static sopt.makers.authentication.common.constant.SystemConstant.SERVICE_NAME_HEADER;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping(PATTERN_ALL)
        .allowedOrigins(CORS_ALLOWED_ORIGINS)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        .allowedHeaders("Authorization", "Content-Type", API_KEY_HEADER, SERVICE_NAME_HEADER)
        .allowCredentials(true)
        .maxAge(3600);
  }
}
