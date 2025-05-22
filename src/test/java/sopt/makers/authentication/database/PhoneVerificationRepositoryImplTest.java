package sopt.makers.authentication.database;

import static org.assertj.core.api.Assertions.assertThat;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
class PhoneVerificationRepositoryImplTest {

  private static final String TEST_PHONE = "01012345678";
  private static final String TEST_CODE = "123456";
  private static final PhoneVerificationType TEST_TYPE = PhoneVerificationType.REGISTER;

  @Autowired private PhoneVerificationRepository phoneVerificationRepository;

  private PhoneVerification createTestVerification() {
    return PhoneVerification.of(null, TEST_PHONE, TEST_TYPE, TEST_CODE);
  }

  @BeforeEach
  void setUp() {
    phoneVerificationRepository.create(createTestVerification());
  }

  @AfterEach
  void tearDown() {
    phoneVerificationRepository.deleteByPhoneVerification(createTestVerification());
  }

  @Test
  @DisplayName("Name이 Null이더라도 정상 조회가 가능하다.")
  void findByVerificationNameNull() {
    // given
    PhoneVerification givenVerification = createTestVerification();

    // when
    PhoneVerification found =
        phoneVerificationRepository.findByPhoneVerification(givenVerification);

    // then
    assertThat(found).isNotNull();
  }
}
