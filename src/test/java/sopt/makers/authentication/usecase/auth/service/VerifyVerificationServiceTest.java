package sopt.makers.authentication.usecase.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase.*;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.support.code.domain.failure.AuthFailure;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class VerifyVerificationServiceTest {

  @Autowired private PhoneVerificationRepository phoneVerificationRepository;
  @Autowired private VerifyVerificationService verifyService;

  @BeforeEach
  void setExistVerification() {
    PhoneVerification testVerification =
        PhoneVerification.of(null, "01012345678", PhoneVerificationType.REGISTER, "123456");
    phoneVerificationRepository.create(testVerification);
  }

  @Test
  void verifySuccessTest() {
    // given
    String givenVerifyPhone = "01012345678";
    PhoneVerificationType givenVerifyType = PhoneVerificationType.REGISTER;
    String givenCode = "123456";
    PhoneVerification givenVerification =
        PhoneVerification.of(null, givenVerifyPhone, givenVerifyType, givenCode);
    VerifyVerificationCommand givenCommand =
        new VerifyVerificationCommand(null, givenVerifyPhone, givenCode, givenVerifyType);

    // when
    VerifyVerificationResult result = verifyService.verify(givenCommand);

    // then
    assertThat(result.isSuccess()).isTrue();
    assertThatThrownBy(() -> phoneVerificationRepository.findByPhoneVerification(givenVerification))
        .isInstanceOf(AuthException.class)
        .hasMessageContaining(AuthFailure.NOT_FOUND_PHONE_VERIFICATION.getMessage());
  }
}
