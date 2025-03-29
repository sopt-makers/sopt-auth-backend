package sopt.makers.authentication.support.config;

import sopt.makers.authentication.support.value.*;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "CustomApplicationConfig")
@ConfigurationPropertiesScan(basePackages = {"sopt.makers.authentication.support.value"})
@EnableConfigurationProperties({
  GabiaProperty.class,
  JwtProperty.class,
  AppleOAuthProperty.class,
  GoogleOAuthProperty.class
})
public class ApplicationConfig {}
