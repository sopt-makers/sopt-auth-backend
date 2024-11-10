package sopt.makers.authentication.support.jwt.provider;

import static sopt.makers.authentication.support.constant.JwtConstant.TOKEN_HEADER;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

  public String extract(String token) {
    return token.substring(TOKEN_HEADER.length());
  }

  public String addPrefix(String token) {
    return TOKEN_HEADER + token;
  }
}
