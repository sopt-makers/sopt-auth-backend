package sopt.makers.authentication.support.config;

import sopt.makers.authentication.support.value.AuthProperty;
import sopt.makers.authentication.support.value.GabiaProperty;
import sopt.makers.authentication.support.value.JwtProperty;
import sopt.makers.authentication.support.value.MakersProperty;

import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration(value = "CustomApplicationConfig")
@ConfigurationPropertiesScan(basePackages = {"sopt.makers.authentication.support.value"})
@EnableConfigurationProperties({
  AuthProperty.class,
  GabiaProperty.class,
  JwtProperty.class,
  MakersProperty.class
})
public class ApplicationConfig {}
