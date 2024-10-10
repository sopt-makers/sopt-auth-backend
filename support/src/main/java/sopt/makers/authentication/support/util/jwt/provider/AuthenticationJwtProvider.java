package sopt.makers.authentication.support.util.jwt.provider;

import org.springframework.stereotype.Component;
import sopt.makers.authentication.support.system.security.authentication.CustomAuthentication;
import sopt.makers.authentication.support.util.jwt.JwtProvider;

import java.io.IOException;

@Component
public class AuthenticationJwtProvider implements JwtProvider<CustomAuthentication> {

    @Override
    public String generate(final CustomAuthentication value) {
        return null;
    }

    @Override
    public CustomAuthentication parse(final String token) throws IOException {
        return null;
    }

}
