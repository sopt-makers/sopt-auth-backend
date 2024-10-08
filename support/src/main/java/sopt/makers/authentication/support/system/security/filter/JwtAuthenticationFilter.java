package sopt.makers.authentication.support.system.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sopt.makers.authentication.support.system.security.authentication.CustomAuthentication;
import sopt.makers.authentication.support.util.jwt.provider.AuthenticationJwtProvider;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final AuthenticationJwtProvider authJwtProvider;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String authorizationToken = getAuthorizationToken(request);
        CustomAuthentication authentication = authJwtProvider.parse(authorizationToken);

        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private String getAuthorizationToken(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION).substring(HttpHeaders.AUTHORIZATION.length()).trim();
    }

}
