package sopt.makers.authentication.support.value;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.gabia")
public record GabiaProperty(Sms sms) {
  public record Sms(String id, String key, String url, String phone) {}
}
