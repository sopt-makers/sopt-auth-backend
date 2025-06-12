package sopt.makers.authentication.domain.auth;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
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
        PhoneVerification.of(
            1L,
            "test",
            "01012345678",
            PhoneVerificationType.REGISTER,
            "123456",
            LocalDateTime.now(),
            false);
  }

  @Test
  @DisplayName("동일한 필드(이름, 번호, 코드, 인증 타입)을 가질 경우, Equals 연산 시 참을 반환한다.")
  void testEqualsTrue() {
    // given
    PhoneVerification givenPhoneVerification =
        PhoneVerification.of(
            1L,
            "test",
            "01012345678",
            PhoneVerificationType.REGISTER,
            testPhoneVerification.getVerificationCode().getCode(),
            testPhoneVerification.getCreatedAt(),
            false);

    // when
    boolean result = testPhoneVerification.equals(givenPhoneVerification);

    // then
    assertThat(result).isTrue();
  }

  @ParameterizedTest
  @MethodSource("argsForNotEqualPhoneVerification")
  @DisplayName("한 개라도 필드의 값이 다를 경우, Equals 연산 시 거짓을 반환한다.")
  void testEqualsFalse(PhoneVerification givenPhoneVerification) {
    // when
    boolean result = testPhoneVerification.equals(givenPhoneVerification);

    // then
    assertThat(result).isFalse();
  }

  static Stream<Arguments> argsForNotEqualPhoneVerification() {
    LocalDateTime now = LocalDateTime.now();
    return Stream.of(
        Arguments.of(
            PhoneVerification.of(
                1L, "testA", "01012345678", PhoneVerificationType.REGISTER, "123456", now, false)),
        Arguments.of(
            PhoneVerification.of(
                1L, "test", "01011345678", PhoneVerificationType.REGISTER, "123456", now, false)),
        Arguments.of(
            PhoneVerification.of(
                1L,
                "test",
                "01012345678",
                PhoneVerificationType.CHANGE_SOCIAL_PLATFORM,
                "123456",
                now,
                false),
            Arguments.of(
                PhoneVerification.of(
                    1L,
                    "test",
                    "01012345678",
                    PhoneVerificationType.REGISTER,
                    "123455",
                    now,
                    false)),
            Arguments.of(
                PhoneVerification.of(
                    2L,
                    "test",
                    "01012345678",
                    PhoneVerificationType.REGISTER,
                    "123456",
                    now,
                    false)),
            Arguments.of(
                PhoneVerification.of(
                    1L,
                    "test",
                    "01012345678",
                    PhoneVerificationType.REGISTER,
                    "123456",
                    now.plusMinutes(1),
                    false)),
            Arguments.of(
                PhoneVerification.of(
                    1L,
                    "test",
                    "01012345678",
                    PhoneVerificationType.REGISTER,
                    "123456",
                    now,
                    true))));
  }

  @Test
  @DisplayName("updateIsVerified 메서드는 isVerified가 true인 새로운 인스턴스를 반환한다.")
  void testUpdateIsVerified() {
    // when
    PhoneVerification updatedVerification = testPhoneVerification.updateIsVerified();

    // then
    assertThat(updatedVerification.isVerified()).isTrue();
    assertThat(updatedVerification).isNotSameAs(testPhoneVerification);
    assertThat(updatedVerification.getId()).isEqualTo(testPhoneVerification.getId());
    assertThat(updatedVerification.getName()).isEqualTo(testPhoneVerification.getName());
    assertThat(updatedVerification.getPhone()).isEqualTo(testPhoneVerification.getPhone());
    assertThat(updatedVerification.getVerificationType())
        .isEqualTo(testPhoneVerification.getVerificationType());
    assertThat(updatedVerification.getVerificationCode())
        .isEqualTo(testPhoneVerification.getVerificationCode());
    assertThat(updatedVerification.getCreatedAt()).isEqualTo(testPhoneVerification.getCreatedAt());
  }
}
