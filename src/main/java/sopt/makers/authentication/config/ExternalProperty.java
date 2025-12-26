package sopt.makers.authentication.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@ConfigurationProperties(prefix = "external")
public record ExternalProperty(Gabia gabia, OAuth oauth, Playground playground, App app, S3 s3) {

  public record Gabia(Sms sms) {
    public record Sms(String id, String key, String url, String phone) {}
  }

  public record OAuth(
      Apple apple, Google google, @JsonProperty("magic-login") MagicLogin magicLogin) {
    public record Apple(String webAud, String appAud) {}

    public record Google(Client client) {
      public record Client(String id) {}
    }

    public record MagicLogin(String phone, String code, String name) {}
  }

  public record Playground(String key, String url) {}

  public record App(String key, String url) {}

  public record S3(String region, Jwt jwt) {
    public record Jwt(String bucket, String publicKeyPath, String privateKeyPath) {}
  }
}
