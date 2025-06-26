package sopt.makers.authentication.adapter.out.external.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.oauth.apple")
public record AppleOAuthProperty(String webAud, String appAud) {}
