package sopt.makers.authentication.adapter.in.web.security.filter;

import static sopt.makers.authentication.common.constant.SystemConstant.WHITELIST_WILDCARD;
import static sopt.makers.authentication.domain.auth.exception.AuthFailure.MISSING_AUTHORIZATION_HEADER;

import sopt.makers.authentication.adapter.in.web.security.authentication.ApiKeyAuthentication;
import sopt.makers.authentication.adapter.in.web.security.authentication.CustomAuthentication;
import sopt.makers.authentication.adapter.out.jwt.service.JwtAuthAccessTokenService;
import sopt.makers.authentication.domain.auth.exception.AuthException;

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

  private final JwtAuthAccessTokenService authTokenProvider;

  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {
    String authorizationToken = getAuthorizationToken(request);
    CustomAuthentication authentication = authTokenProvider.parse(authorizationToken);

    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    filterChain.doFilter(request, response);
  }

  private String getAuthorizationToken(final HttpServletRequest request) {
    String authorizationHeaderValue = request.getHeader(HttpHeaders.AUTHORIZATION);
    if (authorizationHeaderValue == null) {
      throw new AuthException(MISSING_AUTHORIZATION_HEADER);
    }
    return authorizationHeaderValue.trim();
  }

  @Override
  public boolean shouldNotFilter(HttpServletRequest request) {
    return isWhiteRequest(request) || isApiKeyAuthenticationExists();
  }

  private boolean isApiKeyAuthenticationExists() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication instanceof ApiKeyAuthentication;
  }

  private boolean isWhiteRequest(final HttpServletRequest request) {
    String uri = request.getRequestURI();
    return WHITELIST_WILDCARD.stream().anyMatch(uri::startsWith);
  }
}
