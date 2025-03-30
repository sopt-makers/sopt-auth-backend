package sopt.makers.authentication.support.value;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
public class SecurityPropertyTest {

  @Autowired private SecurityProperty securityProperty;

  @Test
  @DisplayName("SecurityProperty 값 확인")
  void checkSecurityProperty() {
    assertThat(securityProperty).isNotNull();
    assertThat(securityProperty.api().key()).isNotNull();
    assertThat(securityProperty.api().securedEndpoints()).isNotNull();
    assertThat(securityProperty.jwt().secret()).isNotNull();
    assertThat(securityProperty.jwt().secret().rsa()).isNotNull();
    assertThat(securityProperty.jwt().secret().expiration()).isNotNull();
    assertThat(securityProperty.jwt().secret().issuer()).isNotNull();
  }
}
