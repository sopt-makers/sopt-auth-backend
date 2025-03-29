package sopt.makers.authentication.support.security.filter;

import java.io.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;

import org.springframework.web.filter.*;

public class ApiKeyAuthenticationFilter extends OncePerRequestFilter {

  private static final String API_KEY_HEADER = "x-api-key";

  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    String requestUri = request.getRequestURI();

    // API Key 검증이 필요한 엔드포인트인지 확인
    if (requestUri.startsWith("/api/public-key")) {
      String apiKey = request.getHeader(API_KEY_HEADER);

      if (apiKey == null || !apiKey.equals(validApiKey)) {
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid API Key");
        return;
      }
    }

    filterChain.doFilter(request, response);
  }
}
