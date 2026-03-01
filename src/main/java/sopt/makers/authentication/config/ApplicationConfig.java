package sopt.makers.authentication.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
  ApplicationProperty.class,
  ExternalProperty.class,
  SecurityProperty.class
})
public class ApplicationConfig {}
