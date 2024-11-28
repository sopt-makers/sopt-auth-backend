package sopt.makers.authentication.support.util;

import static sopt.makers.authentication.support.constant.JwtConstant.REFRESH_TOKEN_HEADER;
import static sopt.makers.authentication.support.constant.SystemConstant.PATTERN_ROOT_PATH;

import sopt.makers.authentication.support.value.JwtProperty;

import java.time.Duration;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class CookieUtil {
  private final JwtProperty jwtProperty;
  private static final String SAME_SITE_NONE = "None";

  public HttpHeaders setRefreshToken(String refreshToken) {
    long durationMillis = jwtProperty.secret().expiration().refreshTokenExpiration();
    Duration duration = Duration.ofMillis(durationMillis);
    ResponseCookie cookie =
        ResponseCookie.from(REFRESH_TOKEN_HEADER, refreshToken)
            .httpOnly(true)
            .secure(true)
            .sameSite(SAME_SITE_NONE)
            .path(PATTERN_ROOT_PATH)
            .maxAge(duration)
            .build();
    HttpHeaders headers = new HttpHeaders();

    headers.add(HttpHeaders.SET_COOKIE, cookie.toString());
    return headers;
  }
}
