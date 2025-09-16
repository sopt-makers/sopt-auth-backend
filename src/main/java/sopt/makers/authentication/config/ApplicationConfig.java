package sopt.makers.authentication.config;

import sopt.makers.authentication.adapter.out.external.app.AppProperty;
import sopt.makers.authentication.adapter.out.external.oauth.AppleOAuthProperty;
import sopt.makers.authentication.adapter.out.external.oauth.GoogleOAuthProperty;
import sopt.makers.authentication.adapter.out.external.oauth.MagicLoginProperty;
import sopt.makers.authentication.adapter.out.external.playground.PlaygroundProperty;
import sopt.makers.authentication.adapter.out.external.sms.GabiaProperty;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
  GabiaProperty.class,
  SecurityProperty.class,
  AppleOAuthProperty.class,
  GoogleOAuthProperty.class,
  MagicLoginProperty.class,
  PlaygroundProperty.class,
  AppProperty.class
})
public class ApplicationConfig {}
