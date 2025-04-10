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

  @Autowired private PhoneVerificationRepository phoneVerificationRepository;

  @BeforeEach
  void initTestPhoneVerification() {
    PhoneVerification testVerification =
        PhoneVerification.of(null, "01012345678", PhoneVerificationType.REGISTER, "123456");
    phoneVerificationRepository.create(testVerification);
  }

  @AfterEach
  void flushTestPhoneVerification() {
    PhoneVerification testVerification =
        PhoneVerification.of(null, "01012345678", PhoneVerificationType.REGISTER, "123456");
    phoneVerificationRepository.deleteByPhoneVerification(testVerification);
  }

  @Test
  @DisplayName("Name이 Null이더라도 정상 조회가 가능하다.")
  void findByVerificationNameNull() {
    // given
    PhoneVerification givenVerification =
        PhoneVerification.of(null, "01012345678", PhoneVerificationType.REGISTER, "123456");
    PhoneVerification findVerification =
        phoneVerificationRepository.findByPhoneVerification(givenVerification);

    // when
    boolean result = givenVerification.equals(findVerification);

    // then
    assertThat(result).isTrue();
  }
}
