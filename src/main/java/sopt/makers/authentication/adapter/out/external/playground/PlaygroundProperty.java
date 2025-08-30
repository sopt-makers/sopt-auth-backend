package sopt.makers.authentication.adapter.out.external.playground;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "external.playground")
public record PlaygroundProperty(String key, String url) {}
