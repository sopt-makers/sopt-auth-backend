package sopt.makers.authentication.adapter.out.external.app;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.app")
public record AppProperty(String key, String url) {}
