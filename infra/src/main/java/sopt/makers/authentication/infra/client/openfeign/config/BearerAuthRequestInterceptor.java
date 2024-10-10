package sopt.makers.authentication.infra.client.openfeign.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.Getter;

@Getter
public abstract class BearerAuthRequestInterceptor implements RequestInterceptor {

    private final String token;

    public BearerAuthRequestInterceptor(final String token) {
        this.token = token;
    }

    public abstract void apply(RequestTemplate template);
}
