package sopt.makers.authentication.usecase.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase.VerifyVerificationCommand;
import static sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase.VerifyVerificationResult;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
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
  void 번호인증시_정상적으로_인증이_완료된다() {
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
    assertThat(result.targetPhone()).isEqualTo(givenVerifyPhone);
    assertThat(result.targetName()).isEqualTo(null);
  }

  @Test
  void 여러개의_인증이력이_존재하더라도_최신인증이력으로_검증한다() {
    // given
    PhoneVerification latestVerification =
        PhoneVerification.of(null, "01012345678", PhoneVerificationType.REGISTER, "123457");
    phoneVerificationRepository.create(latestVerification);
    String givenVerifyPhone = "01012345678";
    PhoneVerificationType givenVerifyType = PhoneVerificationType.REGISTER;
    String givenCode = "123457";
    VerifyVerificationCommand givenCommand =
        new VerifyVerificationCommand(null, givenVerifyPhone, givenCode, givenVerifyType);

    // when
    VerifyVerificationResult result = verifyService.verify(givenCommand);

    // then
    assertThat(result.targetPhone()).isEqualTo(givenVerifyPhone);
  }
}
