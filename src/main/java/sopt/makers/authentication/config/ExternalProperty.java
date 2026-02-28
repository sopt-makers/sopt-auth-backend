package sopt.makers.authentication.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.fasterxml.jackson.annotation.JsonProperty;

@ConfigurationProperties(prefix = "external")
public record ExternalProperty(Sms sms, OAuth oauth, Playground playground, App app, Aws aws) {

  public record Sms(Internal internal) {
    public record Internal(String url, String apiKey) {}
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

  public record Aws(
      String region,
      @JsonProperty("access-key") String accessKey,
      @JsonProperty("secret-key") String secretKey,
      S3 s3) {
    public record S3(String bucket, String publicKeyPath, String privateKeyPath) {}
  }
}
