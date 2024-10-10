package sopt.makers.authentication.infra.client.openfeign.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("external.sopt-makers")
public record ClientProperty(
    Playground playground
) {
    public record Playground(
            String url,
            String token
    ){}

}
