package sopt.makers.authentication.support.jwt.provider;

import sopt.makers.authentication.support.jwt.JwtProvider;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;

import java.io.IOException;

import org.springframework.stereotype.Component;

@Component
public class JwtAuthTokenProvider implements JwtProvider<CustomAuthentication> {

  @Override
  public String generate(CustomAuthentication value) {
    return null;
  }

  @Override
  public CustomAuthentication parse(String token) throws IOException {
    return null;
  }
}
