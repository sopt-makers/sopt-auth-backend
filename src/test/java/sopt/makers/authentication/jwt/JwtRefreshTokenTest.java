package sopt.makers.authentication.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import sopt.makers.authentication.support.common.exception.TokenException;
import sopt.makers.authentication.support.jwt.provider.JwtAuthRefreshTokenProvider;
import sopt.makers.authentication.support.jwt.provider.JwtTokenUtil;

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

@SpringBootTest
@ActiveProfiles("test")
public class JwtRefreshTokenTest {

  @Autowired private JwtAuthRefreshTokenProvider jwtAuthRefreshTokenProvider;

  @Autowired private JwtEncoder jwtEncoder;

  @Autowired private JwtDecoder jwtDecoder;

  @Autowired private JwtTokenUtil jwtTokenUtil;

  @Test
  @DisplayName("RefreshToken 생성")
  public void generate_jwt_refresh_token() {
    // When
    String refreshToken = jwtAuthRefreshTokenProvider.generate("");

    // Then
    System.out.println("RefreshToken: [" + refreshToken + "]");
  }

  @Test
  @DisplayName("RefreshToken 디코딩")
  public void decode_jwt_refresh_token() {
    // Given
    String refreshToken = jwtAuthRefreshTokenProvider.generate("");
    String pureToken = jwtTokenUtil.extract(refreshToken);

    // When
    System.out.println("PureToken: [" + pureToken + "]");
    Jwt jwt = jwtDecoder.decode(pureToken);

    // then
    System.out.println("Jwt: [ " + jwt.getClaims() + " ]");
  }

  @Test
  @DisplayName("RefreshToken 갱신")
  public void refresh_jwt_refresh_token() throws IOException {
    // Given
    String token = jwtAuthRefreshTokenProvider.generate("");
    String pureToken = jwtTokenUtil.extract(token);

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
    String token = jwtTokenUtil.addPrefix(jwt.getTokenValue());
    System.out.println("Token: [" + token + "]");

    // When & then
    assertThatThrownBy(() -> jwtAuthRefreshTokenProvider.parse(token))
        .isInstanceOf(TokenException.class);
  }
}
