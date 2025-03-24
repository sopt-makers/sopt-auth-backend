package sopt.makers.authentication.usecase.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase.CreateVerificationCommand;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.message.Message;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.message.port.out.MessageSendPort;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
class CreateVerificationServiceTest {
  private static final String TEST_NAME_REGISTER_INFO = "TEST REGISTER INFO";
  private static final String TEST_NAME_USER = "TEST USER";
  private static final String TEST_PHONE_REGISTER_INFO = "01087654321";
  private static final String TEST_PHONE_USER = "01012345678";

  @Mock UserRepository userRepository;
  @Mock UserRegisterInfoRepository userRegisterInfoRepository;
  @Mock MessageSendPort sendPort;
  @Autowired PhoneVerificationRepository verificationRepository;

  @InjectMocks CreateVerificationService usecase;

  @BeforeEach
  void setUsers() {
    User mockedUser = mock(User.class);
    UserRegisterInfo mockedUserRegisterInfo = mock(UserRegisterInfo.class);
    when(mockedUser.getProfile())
        .thenReturn(new Profile("TEST USER", null, "01012345678", LocalDate.now(), null));
    when(mockedUserRegisterInfo.getName()).thenReturn("TEST REGISTER INFO");
    when(mockedUserRegisterInfo.getPhone()).thenReturn("01087654321");

    when(userRepository.findByPhone(anyString())).thenReturn(mockedUser);
    when(userRegisterInfoRepository.findByPhone(anyString())).thenReturn(mockedUserRegisterInfo);
    doNothing().when(sendPort).sendMessage(isA(Message.class));

    usecase =
        new CreateVerificationService(
            userRepository, userRegisterInfoRepository, verificationRepository, sendPort);
  }

  @Test
  @DisplayName("인증 유형이 REGISTER 일 경우, User Register Info 기반으로 인증 내역이 생성되어야 합니다.")
  void createVerificationRegister() {
    // given
    PhoneVerificationType givenType = PhoneVerificationType.REGISTER;
    CreateVerificationCommand givenCommand =
        new CreateVerificationCommand(null, TEST_PHONE_REGISTER_INFO, givenType);

    // when
    PhoneVerification resultPhoneVerification = usecase.create(givenCommand);

    // then
    assertThat(resultPhoneVerification.getName()).isEqualTo(TEST_NAME_REGISTER_INFO);
    assertThat(resultPhoneVerification.getPhone()).isEqualTo(TEST_PHONE_REGISTER_INFO);
  }

  @Test
  @DisplayName("인증 유형이 CHANGE 일 경우, 기존에 존재하는 User 기반으로 인증 내역이 생성되어야 합니다.")
  void createVerificationChange() {
    // given
    PhoneVerificationType givenType = PhoneVerificationType.CHANGE;
    CreateVerificationCommand givenCommand =
        new CreateVerificationCommand(null, TEST_PHONE_USER, givenType);

    // when
    PhoneVerification resultPhoneVerification = usecase.create(givenCommand);

    // then
    assertThat(resultPhoneVerification.getName()).isEqualTo(TEST_NAME_USER);
    assertThat(resultPhoneVerification.getPhone()).isEqualTo(TEST_PHONE_USER);
  }

  @Test
  @DisplayName("인증 유형이 SEARCH 일 경우, 기존에 존재하는 User 기반으로 인증 내역이 생성되어야 합니다.")
  void createVerificationSearch() {
    // given
    PhoneVerificationType givenType = PhoneVerificationType.SEARCH;
    CreateVerificationCommand givenCommand =
        new CreateVerificationCommand(null, TEST_PHONE_USER, givenType);

    // when
    PhoneVerification resultPhoneVerification = usecase.create(givenCommand);

    // then
    assertThat(resultPhoneVerification.getName()).isEqualTo(TEST_NAME_USER);
    assertThat(resultPhoneVerification.getPhone()).isEqualTo(TEST_PHONE_USER);
  }
}
