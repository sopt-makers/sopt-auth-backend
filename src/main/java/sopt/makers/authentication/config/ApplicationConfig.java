package sopt.makers.authentication.config;

import sopt.makers.authentication.support.value.AppleOAuthProperty;
import sopt.makers.authentication.support.value.GabiaProperty;
import sopt.makers.authentication.support.value.GoogleOAuthProperty;
import sopt.makers.authentication.support.value.SecurityProperty;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "CustomApplicationConfig")
@ConfigurationPropertiesScan(basePackages = {"sopt.makers.authentication.support.value"})
@EnableConfigurationProperties({
  GabiaProperty.class,
  SecurityProperty.class,
  AppleOAuthProperty.class,
  GoogleOAuthProperty.class
})
public class ApplicationConfig {}
