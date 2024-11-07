package sopt.makers.authentication.support.jwt.provider;

import sopt.makers.authentication.support.jwt.JwtProvider;
import sopt.makers.authentication.support.jwt.token.JwtAccessToken;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;

import java.io.IOException;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthAccessTokenProvider implements JwtProvider<CustomAuthentication> {

  private final JwtTokenUtil tokenUtil;
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  @Override
  public String generate(CustomAuthentication authentication) {
    JwtAccessToken jwtAccessToken = JwtAccessToken.createAccessToken(authentication, jwtEncoder);
    return tokenUtil.addPrefix(jwtAccessToken.getTokenValue());
  }

  @Override
  public CustomAuthentication parse(String requestToken) throws IOException {
    String token = tokenUtil.extract(requestToken);
    JwtAccessToken jwtAccessToken = JwtAccessToken.fromStringToken(token, jwtDecoder);
    return jwtAccessToken.parse();
  }
}
