package sopt.makers.authentication.support.security.filter;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.INVALID_API_KEY;

import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.support.security.authentication.ApiKeyAuthentication;
import sopt.makers.authentication.support.value.SecurityProperty;

import java.io.IOException;
import java.util.List;

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
  private static final String API_KEY_HEADER = "x-api-key";
  private static final String PRODUCT = "product";
  private final SecurityProperty securityProperty;

  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {
    String requestUri = request.getRequestURI();
    List<String> securedEndpoints = securityProperty.api().securedEndpoints();

    for (String endpoint : securedEndpoints) {
      if (requestUri.startsWith(endpoint)) {
        String apiKey = request.getHeader(API_KEY_HEADER);
        String product = request.getHeader(PRODUCT);
        boolean isApiKeyInvalid = apiKey == null || !apiKey.equals(securityProperty.api().key());

        if (isApiKeyInvalid) {
          throw new AuthException(INVALID_API_KEY);
        }
        SecurityContextHolder.getContext()
            .setAuthentication(new ApiKeyAuthentication(apiKey, product));
        break;
      }
    }
    filterChain.doFilter(request, response);
  }
}
