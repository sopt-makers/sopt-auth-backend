package sopt.makers.authentication.config;

import static sopt.makers.authentication.common.constant.CookieConstant.CORS_ALLOWED_ORIGIN_PATTERN;
import static sopt.makers.authentication.common.constant.SystemConstant.PATTERN_ALL;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

  @Override
  public void addCorsMappings(CorsRegistry registry) {
    registry
        .addMapping(PATTERN_ALL)
        .allowedOriginPatterns(CORS_ALLOWED_ORIGIN_PATTERN)
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
        .allowedHeaders("Authorization", "Content-Type")
        .allowCredentials(true)
        .maxAge(3600);
  }
}
