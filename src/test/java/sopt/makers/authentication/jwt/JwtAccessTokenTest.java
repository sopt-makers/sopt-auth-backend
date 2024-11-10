package sopt.makers.authentication.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import sopt.makers.authentication.support.jwt.provider.JwtAuthAccessTokenProvider;
import sopt.makers.authentication.support.jwt.provider.JwtTokenUtil;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;

import java.io.IOException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class JwtAccessTokenTest {

  private static final Logger log = LoggerFactory.getLogger(JwtAccessTokenTest.class);
  @Autowired private JwtAuthAccessTokenProvider jwtAuthAccessTokenProvider;

  @Autowired private JwtEncoder jwtEncoder;

  @Autowired private JwtDecoder jwtDecoder;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Test
  @DisplayName("AccessToken 생성")
  void create_jwt_access_token() {
    // Given
    CustomAuthentication customAuthentication = new CustomAuthentication("test", "test");
    // When
    String accessToken = jwtAuthAccessTokenProvider.generate(customAuthentication);

    // Then
    System.out.println("AccessToken: [" + accessToken + "]");
    assertThat(accessToken).isNotNull();
  }

  @Test
  @DisplayName("AccessToken 디코딩")
  void decode_jwt_access_token() {
    // Given
    CustomAuthentication customAuthentication = new CustomAuthentication("test", "test");
    String accessToken = jwtAuthAccessTokenProvider.generate(customAuthentication);
    String pureToken = jwtTokenUtil.extract(accessToken);

    // When
    System.out.println("PureToken: [" + pureToken + "]");
    Jwt jwt = jwtDecoder.decode(pureToken);

    // Then
    assertThat(jwt.getClaims().get("iss")).isEqualTo("operation");
    assertThat(jwt.getClaims().get("sub")).isEqualTo("test");
  }

  @Test
  @DisplayName("AccessToken 파싱")
  void parse_jwt_access_token() throws IOException {
    // Given
    CustomAuthentication customAuthentication = new CustomAuthentication("test", "test");
    String accessToken = jwtAuthAccessTokenProvider.generate(customAuthentication);

    // when
    CustomAuthentication parsedAuthentication = jwtAuthAccessTokenProvider.parse(accessToken);

    // then
    assertThat(parsedAuthentication).isNotNull();
    assertThat(parsedAuthentication.getPrincipal().toString())
        .isEqualTo(customAuthentication.getPrincipal().toString());
  }

  @Test
  @DisplayName("토큰 생성 후 접두사 검증")
  void add_prefix() {
    // Given
    String givenToken = "test";
    String expectedToken = "Bearer test";

    // When
    String completionToken = jwtTokenUtil.addPrefix(givenToken);

    // then
    assertThat(completionToken).isEqualTo(expectedToken);
  }

  @Test
  @DisplayName("토큰 파싱 전 접두사 제거 검증")
  void extract_prefix() {
    // Given
    String tokenWithHeader = "Bearer SomeToken";
    String expectedToken = "SomeToken";

    // when
    String pureToken = jwtTokenUtil.extract(tokenWithHeader);

    // then
    assertThat(pureToken).isEqualTo(expectedToken);
  }
}
