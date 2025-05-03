package sopt.makers.authentication.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static sopt.makers.authentication.support.jwt.provider.JwtTokenUtil.addPrefix;
import static sopt.makers.authentication.support.jwt.provider.JwtTokenUtil.extract;

import sopt.makers.authentication.support.jwt.provider.JwtAuthAccessTokenProvider;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;
import sopt.makers.authentication.usecase.auth.port.in.JwksRetrieveUsecase;

import java.io.IOException;
import java.security.interfaces.RSAPublicKey;
import java.text.ParseException;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jwt.SignedJWT;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
public class JwtAccessTokenTest {
  @Autowired private JwtAuthAccessTokenProvider jwtAuthAccessTokenProvider;
  @Autowired private JwtDecoder jwtDecoder;
  @Autowired private JwksRetrieveUsecase jwksRetrieveUsecase;

  @Value("${security.jwt.secret.rsa.key-id}")
  private String keyId;

  public final String TOKEN_HEADER = "Bearer ";

  @Test
  @DisplayName("AccessToken 생성")
  void create_jwt_access_token() {
    // Given
    CustomAuthentication customAuthentication = new CustomAuthentication("test", "test");

    // When
    String accessToken = jwtAuthAccessTokenProvider.generateJwt(customAuthentication);

    // Then
    assertThat(accessToken).isNotNull();
  }

  @Test
  @DisplayName("AccessToken 디코딩")
  void decode_jwt_access_token() {
    // Given
    CustomAuthentication customAuthentication = new CustomAuthentication("test", "test");
    String accessToken = jwtAuthAccessTokenProvider.generateJwt(customAuthentication);

    // When
    Jwt jwt = jwtDecoder.decode(accessToken);

    // Then
    assertThat(jwt.getClaims().get("iss")).isEqualTo("authentication");
    assertThat(jwt.getClaims().get("sub")).isEqualTo("test");
  }

  @Test
  @DisplayName("AccessToken 파싱")
  void parse_jwt_access_token() throws IOException {
    // Given
    CustomAuthentication customAuthentication = new CustomAuthentication("test", "test");
    String accessToken = jwtAuthAccessTokenProvider.generateJwt(customAuthentication);

    // when
    CustomAuthentication parsedAuthentication =
        jwtAuthAccessTokenProvider.parse(TOKEN_HEADER + accessToken);

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
    String completionToken = addPrefix(givenToken);

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
    String pureToken = extract(tokenWithHeader);

    // then
    assertThat(pureToken).isEqualTo(expectedToken);
  }

  @Test
  @DisplayName("JwtAuthService로 Jwk를 조회하고, Public key로 AccessToken 디코딩")
  void decode_jwt_access_token_with_public_key() throws ParseException, JOSEException {
    // Arrange
    CustomAuthentication customAuthentication = new CustomAuthentication("test", "test");
    String accessToken = jwtAuthAccessTokenProvider.generateJwt(customAuthentication);

    SignedJWT signedJWT = SignedJWT.parse(accessToken);
    JWKSet info = jwksRetrieveUsecase.retrievePublicKey();
    RSAKey rsaKey = (RSAKey) info.getKeyByKeyId(keyId);
    RSAPublicKey publicKey = rsaKey.toRSAPublicKey();

    // Act
    JWSVerifier verifier = new RSASSAVerifier(publicKey);
    boolean isValid = signedJWT.verify(verifier);

    // Assert
    assertThat(isValid).isTrue();
  }
}
