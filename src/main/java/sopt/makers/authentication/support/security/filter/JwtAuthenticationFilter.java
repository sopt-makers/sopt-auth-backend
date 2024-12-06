package sopt.makers.authentication.support.security.filter;

import static sopt.makers.authentication.support.constant.SystemConstant.PATH_ACTUATOR;
import static sopt.makers.authentication.support.constant.SystemConstant.PATH_AUTH;
import static sopt.makers.authentication.support.constant.SystemConstant.PATH_ERROR;
import static sopt.makers.authentication.support.constant.SystemConstant.PATH_TEST;

import sopt.makers.authentication.support.jwt.provider.JwtAuthAccessTokenProvider;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtAuthAccessTokenProvider authTokenProvider;

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) {
    return checkIsWhiteURI(request.getRequestURI());
  }

  private boolean checkIsWhiteURI(String uri) {
    return uri.contains(PATH_ACTUATOR)
        && uri.contains(PATH_AUTH)
        && uri.contains(PATH_ERROR)
        && uri.contains(PATH_TEST);
  }

  @Override
  protected void doFilterInternal(
      final HttpServletRequest request, final HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationToken = getAuthorizationToken(request);
    CustomAuthentication authentication = authTokenProvider.parse(authorizationToken);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);

    filterChain.doFilter(request, response);
  }

  private String getAuthorizationToken(final HttpServletRequest request) {
    String authorizationHeaderValue =
        request.getHeader(HttpHeaders.AUTHORIZATION).substring(HttpHeaders.AUTHORIZATION.length());
    String authorizationToken = authorizationHeaderValue.trim();
    return authorizationToken;
  }
}
