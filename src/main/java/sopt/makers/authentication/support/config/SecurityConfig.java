package sopt.makers.authentication.support.config;

import static sopt.makers.authentication.support.constant.SystemConstant.PATTERN_ACTUATOR;
import static sopt.makers.authentication.support.constant.SystemConstant.PATTERN_AUTH;
import static sopt.makers.authentication.support.constant.SystemConstant.PATTERN_ERROR_PATH;
import static sopt.makers.authentication.support.constant.SystemConstant.PATTERN_TEST;

import sopt.makers.authentication.support.security.filter.ApiKeyAuthenticationFilter;
import sopt.makers.authentication.support.security.filter.JwtAuthenticationFilter;
import sopt.makers.authentication.support.security.filter.JwtExceptionFilter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private static final String ALL = "*";

  private final ApiKeyAuthenticationFilter apiKeyAuthenticationFilter;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;
  private final JwtExceptionFilter jwtExceptionFilter;

  @Bean
  public static PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  @Profile({"local", "test"})
  public SecurityFilterChain filterChainLocalAndTest(HttpSecurity http) throws Exception {
    setDefaultHttp(http);
    http.authorizeHttpRequests(
        authorizeHttpRequests -> authorizeHttpRequests.anyRequest().permitAll());
    return http.build();
  }

  @Bean
  @Profile("dev")
  public SecurityFilterChain filterChainDev(HttpSecurity http) throws Exception {
    setDefaultHttp(http);
    http.authorizeHttpRequests(
        authorizeHttpRequests ->
            authorizeHttpRequests.requestMatchers(new AntPathRequestMatcher("/v3/**")).permitAll());
    setSecuredHttp(http);
    return http.build();
  }

  @Bean
  @Profile("prod")
  public SecurityFilterChain filterChainProd(HttpSecurity http) throws Exception {
    setDefaultHttp(http);
    setSecuredHttp(http);
    return http.build();
  }

  private void setDefaultHttp(HttpSecurity http) throws Exception {
    http.httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .formLogin(AbstractHttpConfigurer::disable)
        .cors(AbstractHttpConfigurer::disable)
        .sessionManagement(
            configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(apiKeyAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
        .addFilterBefore(jwtExceptionFilter, JwtAuthenticationFilter.class);
  }

  private void setSecuredHttp(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        authorizeHttpRequests ->
            authorizeHttpRequests
                .requestMatchers(new AntPathRequestMatcher(PATTERN_AUTH))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(PATTERN_TEST))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(PATTERN_ERROR_PATH))
                .permitAll()
                .requestMatchers(new AntPathRequestMatcher(PATTERN_ACTUATOR))
                .permitAll()
                .anyRequest()
                .authenticated());
  }
}
