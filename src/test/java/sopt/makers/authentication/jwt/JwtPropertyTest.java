package sopt.makers.authentication.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import sopt.makers.authentication.support.value.JwtProperty;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class JwtPropertyTest {

  @Autowired private JwtProperty jwtProperty;

  @Test
  @DisplayName("JwtProperty 값 확인")
  void checkJwtProperty() {
    assertThat(jwtProperty).isNotNull();
    assertThat(jwtProperty.secret()).isNotNull();
    assertThat(jwtProperty.secret().rsa()).isNotNull();
    assertThat(jwtProperty.secret().expiration()).isNotNull();
    assertThat(jwtProperty.secret().issuer()).isNotNull();
  }
}
