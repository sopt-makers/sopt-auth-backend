package sopt.makers.authentication.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sopt.makers.authentication.support.jwt.provider.JwtTokenUtil.addPrefix;
import static sopt.makers.authentication.support.jwt.provider.JwtTokenUtil.extract;

import sopt.makers.authentication.support.exception.support.TokenException;
import sopt.makers.authentication.support.jwt.provider.JwtAuthRefreshTokenProvider;

import java.io.IOException;
import java.time.Instant;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
public class JwtRefreshTokenTest {

  @Autowired private JwtAuthRefreshTokenProvider jwtAuthRefreshTokenProvider;
  @Autowired private JwtEncoder jwtEncoder;
  @Autowired private JwtDecoder jwtDecoder;

  @Test
  @DisplayName("RefreshToken 생성")
  public void generate_jwt_refresh_token() {
    // Given
    String accessToken = "Bearer ey.d.d";
    String givenToken = jwtAuthRefreshTokenProvider.generate(accessToken);

    // When
    String expectedToken = jwtAuthRefreshTokenProvider.generate(accessToken);

    // Then
    assertThat(givenToken).isNotNull();
    assertThat(givenToken).isNotEqualTo(expectedToken);
  }

  @Test
  @DisplayName("RefreshToken 디코딩")
  public void decode_jwt_refresh_token() {
    // Given
    String accessToken = "Bearer ey.d.d";
    String refreshToken = jwtAuthRefreshTokenProvider.generate(accessToken);
    String pureToken = extract(refreshToken);

    // When
    Jwt jwt = jwtDecoder.decode(pureToken);

    // then
    assertThat(jwt.getClaims()).isNotNull();
  }

  @Test
  @DisplayName("RefreshToken 갱신")
  public void refresh_jwt_refresh_token() throws IOException {
    // Given
    String accessToken = "Bearer ey.d.d";
    String token = jwtAuthRefreshTokenProvider.generate(accessToken);

    // When
    String refreshedToken = jwtAuthRefreshTokenProvider.parse(token);

    // then
    System.out.println("RefreshedToken: [" + refreshedToken + "]");
    assertThat(refreshedToken).isNotEqualTo(token);
  }

  @Test
  @DisplayName("RefreshToken 만료시간 검증")
  public void validate_jwt_refresh_token() throws IOException {
    // Given
    JwtClaimsSet jwtClaimsSet =
        JwtClaimsSet.builder().expiresAt(Instant.now().minusSeconds(1)).build();
    Jwt jwt = jwtEncoder.encode(JwtEncoderParameters.from(jwtClaimsSet));
    String token = addPrefix(jwt.getTokenValue());
    System.out.println("Token: [" + token + "]");

    // When & then
    assertThatThrownBy(() -> jwtAuthRefreshTokenProvider.parse(token))
        .isInstanceOf(TokenException.class);
  }
}
