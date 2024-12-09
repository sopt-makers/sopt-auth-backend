package sopt.makers.authentication.usecase.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.auth.port.in.GetSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("local")
@TestPropertySource(locations = {"classpath:env/local.env"})
class GetSocialAccountPlatformServiceTest {
  private static final String TEST_AUTH_ID = "test";
  private static final String TEST_USER_PHONE_FOR_GOOGLE = "01012345678";
  private static final String TEST_USER_PHONE_FOR_APPLE = "01087654321";

  @MockBean UserRepository userRepository;

  @Autowired GetSocialAccountPlatformService getSocialAccountPlatformService;

  @BeforeEach
  void setMockUser() {
    User mockedUserForGoogle = mock(User.class);
    User mockedUserForApple = mock(User.class);
    SocialAccount mockedSocialAccountForGoogle = mock(SocialAccount.class);
    SocialAccount mockedSocialAccountForApple = mock(SocialAccount.class);

    given(mockedSocialAccountForGoogle.authPlatformId()).willReturn(TEST_AUTH_ID);
    given(mockedSocialAccountForApple.authPlatformId()).willReturn(TEST_AUTH_ID);
    given(mockedSocialAccountForGoogle.authPlatformType()).willReturn(AuthPlatform.GOOGLE);
    given(mockedSocialAccountForApple.authPlatformType()).willReturn(AuthPlatform.APPLE);

    given(mockedUserForGoogle.getSocialAccount()).willReturn(mockedSocialAccountForGoogle);
    given(mockedUserForApple.getSocialAccount()).willReturn(mockedSocialAccountForApple);

    when(userRepository.findByPhone(TEST_USER_PHONE_FOR_GOOGLE)).thenReturn(mockedUserForGoogle);
    when(userRepository.findByPhone(TEST_USER_PHONE_FOR_APPLE)).thenReturn(mockedUserForApple);
  }

  @Test
  void 주어진_Command에_대해_의도한_결과값을_반환한다() {
    // given
    GetSocialAccountUsecase.GetSocialAccountPlatformCommand givenCommandForGoogle =
        new GetSocialAccountUsecase.GetSocialAccountPlatformCommand(
            null, TEST_USER_PHONE_FOR_GOOGLE);
    GetSocialAccountUsecase.GetSocialAccountPlatformCommand givenCommandForApple =
        new GetSocialAccountUsecase.GetSocialAccountPlatformCommand(
            null, TEST_USER_PHONE_FOR_APPLE);

    // when
    GetSocialAccountUsecase.SocialAccountPlatformInfo resultForGoogle =
        getSocialAccountPlatformService.getSocialAccountPlatform(givenCommandForGoogle);
    GetSocialAccountUsecase.SocialAccountPlatformInfo resultForApple =
        getSocialAccountPlatformService.getSocialAccountPlatform(givenCommandForApple);

    // then
    assertThat(resultForGoogle.platformName()).isEqualTo("GOOGLE");
    assertThat(resultForApple.platformName()).isEqualTo("APPLE");
  }
}
