package sopt.makers.authentication.adapter.out.jwt.provider;

import static sopt.makers.authentication.adapter.out.jwt.JwtConstant.TOKEN_HEADER;

public final class JwtTokenUtil {

  private JwtTokenUtil() {}

  public static String extract(String token) {
    return token.substring(TOKEN_HEADER.length());
  }

  public static String addPrefix(String token) {
    return TOKEN_HEADER + token;
  }
}
