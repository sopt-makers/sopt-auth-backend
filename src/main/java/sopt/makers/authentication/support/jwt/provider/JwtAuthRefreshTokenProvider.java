package sopt.makers.authentication.support.jwt.provider;

import sopt.makers.authentication.support.jwt.JwtProvider;
import sopt.makers.authentication.support.jwt.token.JwtRefreshToken;

import java.io.IOException;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthRefreshTokenProvider implements JwtProvider<String> {

  private final JwtTokenUtil tokenUtil;
  private final JwtEncoder jwtEncoder;
  private final JwtDecoder jwtDecoder;

  @Override
  public String generate(String requestToken) {
    JwtRefreshToken jwtRefreshToken = JwtRefreshToken.createRefreshToken(jwtEncoder);
    return tokenUtil.addPrefix(jwtRefreshToken.getTokenValue());
  }

  @Override
  public String parse(String requestToken) throws IOException {
    String token = tokenUtil.extract(requestToken);
    JwtRefreshToken jwtRefreshToken = JwtRefreshToken.fromStringToken(token);
    jwtRefreshToken.validateExpire(jwtDecoder);
    JwtRefreshToken refreshedToken = jwtRefreshToken.refresh(jwtEncoder, jwtDecoder);
    return tokenUtil.addPrefix(refreshedToken.getTokenValue());
  }
}
