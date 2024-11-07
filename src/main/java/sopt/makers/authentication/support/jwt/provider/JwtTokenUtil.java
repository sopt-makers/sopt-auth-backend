package sopt.makers.authentication.support.jwt.provider;

import sopt.makers.authentication.support.constant.JwtConstant;

import org.springframework.stereotype.Component;

@Component
public class JwtTokenUtil {

  public String extract(String token) {
    return token.substring(JwtConstant.TOKEN_HEADER.length());
  }

  public String addPrefix(String token) {
    return JwtConstant.TOKEN_HEADER + token;
  }
}
