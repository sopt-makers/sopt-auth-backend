package sopt.makers.authentication.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "magic-login")
public record MagicLoginProperty(String phone, String code, String name) {}
