package sopt.makers.authentication.adapter.out.external.oauth;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.oauth.magic-login")
public record MagicLoginProperty(String phone, String code, String name) {}
