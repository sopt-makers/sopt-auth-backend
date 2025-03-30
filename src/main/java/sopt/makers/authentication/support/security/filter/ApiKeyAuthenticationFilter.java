package sopt.makers.authentication.support.security.filter;

import static sopt.makers.authentication.support.code.support.failure.CommonFailure.INVALID_API_KEY;
import static sopt.makers.authentication.support.util.ResponseUtil.generateErrorResponse;

import sopt.makers.authentication.support.exception.support.FilterException;
import sopt.makers.authentication.support.value.SecurityProperty;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {
  private static final String API_KEY_HEADER = "x-api-key";
  private final SecurityProperty securityProperty;

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String requestUri = request.getRequestURI();
    List<String> securedEndpoints = securityProperty.api().securedEndpoints();

    for (String endpoint : securedEndpoints) {
      if (requestUri.startsWith(endpoint)) {
        String apiKey = request.getHeader(API_KEY_HEADER);
        boolean isApiKeyInvalid = apiKey == null || !apiKey.equals(securityProperty.api().key());

        if (isApiKeyInvalid) {
          generateErrorResponse(response, new FilterException(INVALID_API_KEY));
          return;
        }
        break;
      }
    }
    filterChain.doFilter(request, response);
  }
}
