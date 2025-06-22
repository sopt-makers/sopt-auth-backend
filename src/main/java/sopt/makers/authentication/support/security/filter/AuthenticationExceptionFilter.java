package sopt.makers.authentication.support.security.filter;

import sopt.makers.authentication.adapter.out.jwt.exception.TokenException;
import sopt.makers.authentication.domain.auth.exception.AuthException;
import sopt.makers.authentication.support.util.ResponseUtil;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class AuthenticationExceptionFilter extends OncePerRequestFilter {

  @Override
  protected void doFilterInternal(
      @NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain filterChain)
      throws ServletException, IOException {
    try {
      filterChain.doFilter(request, response);
    } catch (AuthException | TokenException e) {
      ResponseUtil.generateErrorResponse(response, e);
    }
  }
}
