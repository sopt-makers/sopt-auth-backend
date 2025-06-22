package sopt.makers.authentication.adapter.in.web.security.filter;

import static sopt.makers.authentication.common.constant.SystemConstant.API_KEY_HEADER;
import static sopt.makers.authentication.common.constant.SystemConstant.SERVICE_NAME_HEADER;
import static sopt.makers.authentication.domain.auth.exception.AuthFailure.INVALID_API_KEY;

import sopt.makers.authentication.adapter.in.web.security.authentication.ApiKeyAuthentication;
import sopt.makers.authentication.domain.auth.exception.AuthException;
import sopt.makers.authentication.support.value.SecurityProperty;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

  private final SecurityProperty securityProperty;

  @Override
  protected void doFilterInternal(
      @NonNull HttpServletRequest request,
      @NonNull HttpServletResponse response,
      @NonNull FilterChain filterChain)
      throws ServletException, IOException {

    String uri = request.getRequestURI();
    if (isSecuredEndpoint(uri)) {
      handleApiKeyAuthentication(request);
    }

    filterChain.doFilter(request, response);
  }

  private boolean isSecuredEndpoint(String uri) {
    return securityProperty.api().securedEndpoints().stream().anyMatch(uri::startsWith);
  }

  private void handleApiKeyAuthentication(HttpServletRequest request) {
    String apiKey = request.getHeader(API_KEY_HEADER);
    String serviceName = request.getHeader(SERVICE_NAME_HEADER);

    validateApiKey(apiKey, serviceName);

    SecurityContextHolder.getContext()
        .setAuthentication(new ApiKeyAuthentication(apiKey, serviceName));
  }

  private void validateApiKey(String apiKey, String serviceName) {
    if (apiKey == null || serviceName == null) {
      throw new AuthException(INVALID_API_KEY);
    }

    String expectedKey = securityProperty.api().keys().get(serviceName);
    if (!apiKey.equals(expectedKey)) {
      throw new AuthException(INVALID_API_KEY);
    }
  }
}
