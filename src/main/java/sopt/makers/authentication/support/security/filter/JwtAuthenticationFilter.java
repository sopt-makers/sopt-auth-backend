package sopt.makers.authentication.support.security.filter;

import static sopt.makers.authentication.support.constant.SystemConstant.WHITELIST_WILDCARD;

import sopt.makers.authentication.support.jwt.provider.JwtAuthAccessTokenProvider;
import sopt.makers.authentication.support.security.authentication.ApiKeyAuthentication;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.Authentication;
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
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {
    if (isApiKeyAuthenticationExists()) {
      filterChain.doFilter(request, response);
      return;
    }

    String authorizationToken = getAuthorizationToken(request);
    CustomAuthentication authentication = authTokenProvider.parse(authorizationToken);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  private boolean isApiKeyAuthenticationExists() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication instanceof ApiKeyAuthentication;
  }

  /**
   * @author 강현욱 @hyunw9
   * @return JwtToken Authorization 헤더에서 "Bearer "를 제거하여 토큰을 추출합니다.
   */
  private String getAuthorizationToken(final HttpServletRequest request) {
    String authorizationHeaderValue =
        request.getHeader(HttpHeaders.AUTHORIZATION).substring(HttpHeaders.AUTHORIZATION.length());
    return authorizationHeaderValue.trim();
  }

  @Override
  public boolean shouldNotFilter(HttpServletRequest request) {
    return isWhiteRequest(request);
  }

  private boolean isWhiteRequest(final HttpServletRequest request) {
    String uri = request.getRequestURI();
    return WHITELIST_WILDCARD.stream().anyMatch(uri::startsWith);
  }
}
