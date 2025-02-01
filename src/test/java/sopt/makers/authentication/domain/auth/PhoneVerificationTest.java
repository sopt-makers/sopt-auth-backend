package sopt.makers.authentication.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
class PhoneVerificationTest {

  private PhoneVerification testPhoneVerification;

  @BeforeEach
  void setTestPhoneVerification() {
    this.testPhoneVerification =
        PhoneVerification.of("test", "01012345678", PhoneVerificationType.REGISTER, "123456");
  }

  @Test
  @DisplayName("동일한 필드(이름, 번호, 코드, 인증 타입)을 가질 경우, Equals 연산 시 참을 반환한다.")
  void testEqualsTrue() {
    // given
    PhoneVerification givenPhoneVerification =
        PhoneVerification.of(
            "test",
            "01012345678",
            PhoneVerificationType.REGISTER,
            testPhoneVerification.getVerificationCode().getCode());

    // when
    boolean result = testPhoneVerification.equals(givenPhoneVerification);

    // then
    assertThat(result).isTrue();
  }

  @ParameterizedTest
  @MethodSource("argsForNotEqualPhoneVerification")
  @DisplayName("한 개라도 필드의 값이 다를 경우, Equals 연산 시 거짓을 반환한다.")
  void testEqualsFalse(
      // given
      PhoneVerification givenPhoneVerification) {
    // when
    boolean result = testPhoneVerification.equals(givenPhoneVerification);

    // then
    assertThat(result).isFalse();
  }

  static Stream<Arguments> argsForNotEqualPhoneVerification() {
    return Stream.of(
        Arguments.of(
            PhoneVerification.of("testA", "01012345678", PhoneVerificationType.REGISTER, "123456")),
        Arguments.of(
            PhoneVerification.of("test", "01011345678", PhoneVerificationType.REGISTER, "123456")),
        Arguments.of(
            PhoneVerification.of("test", "01012345678", PhoneVerificationType.CHANGE, "123456")),
        Arguments.of(
            PhoneVerification.of("test", "01012345678", PhoneVerificationType.REGISTER, "123455")));
  }
}
