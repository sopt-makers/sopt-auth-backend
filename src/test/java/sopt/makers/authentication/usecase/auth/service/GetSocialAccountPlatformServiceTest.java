package sopt.makers.authentication.usecase.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.validator.PhoneVerificationValidator;
import sopt.makers.authentication.usecase.auth.port.in.GetSocialAccountUsecase;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;
import sopt.makers.authentication.usecase.user.service.GetSocialAccountPlatformService;

import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
class GetSocialAccountPlatformServiceTest {
  private static final String PLATFORM_NAME_GOOGLE = "GOOGLE";
  private static final String PLATFORM_NAME_APPLE = "APPLE";
  private static final String TEST_AUTH_ID = "test";
  private static final String TEST_USER_PHONE_FOR_GOOGLE = "01012345678";
  private static final String TEST_USER_PHONE_FOR_APPLE = "01087654321";

  @MockBean UserRepository userRepository;

  @MockBean PhoneVerificationValidator phoneVerificationValidator;

  @Autowired GetSocialAccountPlatformService getSocialAccountPlatformService;

  @BeforeEach
  void setMockUser() {
    User mockedUserForGoogle = mock(User.class);
    User mockedUserForApple = mock(User.class);
    Profile mockProfile = mock(Profile.class);

    SocialAccount mockedSocialAccountForGoogle = mock(SocialAccount.class);
    SocialAccount mockedSocialAccountForApple = mock(SocialAccount.class);

    given(mockedSocialAccountForGoogle.authPlatformId()).willReturn(TEST_AUTH_ID);
    given(mockedSocialAccountForApple.authPlatformId()).willReturn(TEST_AUTH_ID);
    given(mockedSocialAccountForGoogle.authPlatformType()).willReturn(AuthPlatform.GOOGLE);
    given(mockedSocialAccountForApple.authPlatformType()).willReturn(AuthPlatform.APPLE);

    given(mockedUserForGoogle.getProfile()).willReturn(mockProfile);
    given(mockedUserForGoogle.getSocialAccount()).willReturn(mockedSocialAccountForGoogle);
    given(mockedUserForApple.getSocialAccount()).willReturn(mockedSocialAccountForApple);
    given(mockedUserForApple.getProfile()).willReturn(mockProfile);

    when(userRepository.findByPhone(TEST_USER_PHONE_FOR_GOOGLE)).thenReturn(mockedUserForGoogle);
    when(userRepository.findByPhone(TEST_USER_PHONE_FOR_APPLE)).thenReturn(mockedUserForApple);

    doNothing()
        .when(phoneVerificationValidator)
        .validate(
            anyString(),
            eq(TEST_USER_PHONE_FOR_GOOGLE),
            eq(PhoneVerificationType.SEARCH_SOCIAL_PLATFORM));
    doNothing()
        .when(phoneVerificationValidator)
        .validate(
            anyString(),
            eq(TEST_USER_PHONE_FOR_APPLE),
            eq(PhoneVerificationType.SEARCH_SOCIAL_PLATFORM));
  }

  @ParameterizedTest(name = "({index}) command : {0} -> result : {1}")
  @DisplayName("주어진 Command에 대해 의도한 결과값을 반환한다")
  @MethodSource("argsForGetPlatformInfoTest")
  void 주어진_Command에_대해_의도한_결과값을_반환한다(
      // given
      GetSocialAccountUsecase.GetSocialAccountPlatformCommand givenCommand, String expectedResult) {

    // when
    GetSocialAccountUsecase.SocialAccountPlatformInfo result =
        getSocialAccountPlatformService.getSocialAccountPlatform(givenCommand);

    // then
    assertThat(result.platformName()).isEqualTo(expectedResult);
  }

  static Stream<Arguments> argsForGetPlatformInfoTest() {
    return Stream.of(
        Arguments.of(
            new GetSocialAccountUsecase.GetSocialAccountPlatformCommand(
                "", TEST_USER_PHONE_FOR_GOOGLE),
            PLATFORM_NAME_GOOGLE),
        Arguments.of(
            new GetSocialAccountUsecase.GetSocialAccountPlatformCommand(
                "", TEST_USER_PHONE_FOR_APPLE),
            PLATFORM_NAME_APPLE));
  }
}
