package sopt.makers.authentication.usecase.auth.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.usecase.auth.port.in.SignUpUsecase.SignUpCommand;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;
import sopt.makers.authentication.usecase.user.port.out.UserActivityHistoryRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;

@ActiveProfiles("test")
@TestPropertySource(locations = {"classpath:env/test.env"})
@ExtendWith(MockitoExtension.class)
class SignUpServiceTest {

  @InjectMocks private SignUpService signUpService;
  @Mock private OAuthAuthenticator oAuthAuthenticator;
  @Mock private UserRegisterInfoRepository userRegisterInfoRepository;
  @Mock private UserRepository userRepository;
  @Mock private UserActivityHistoryRepository userActivityHistoryRepository;
  @Mock private UserRegisterInfo userRegisterInfo;

  private final String TEST_TOKEN = "dummy-token";
  private final String TEST_PHONE = "01012345678";
  private final Long TEST_USER_ID = 42L;

  private final int ACTIVITY_GENERATION = 35;

  @BeforeEach
  void setUpMocks() {
    SocialAccount socialAccount = SocialAccount.of("oauth-123", AuthPlatform.GOOGLE);
    Profile profile = Profile.of("테스터", "tester@sopt.org", TEST_PHONE, LocalDate.of(2000, 1, 1));
    User mockedUser = User.createNewUser(socialAccount, profile);

    ReflectionTestUtils.setField(mockedUser, "id", TEST_USER_ID);

    given(oAuthAuthenticator.getIdentifier(TEST_TOKEN, AuthPlatform.GOOGLE))
        .willReturn("oauth-123");
    given(userRegisterInfoRepository.findByPhone(TEST_PHONE))
        .willReturn(Optional.of(userRegisterInfo));
    given(userRepository.save(any(User.class))).willReturn(mockedUser);
    given(userRegisterInfo.getGeneration()).willReturn(ACTIVITY_GENERATION);
  }

  @Test
  void 회원가입시_정상적으로_유저와_기수정보가_저장된다() {
    // given
    SignUpCommand command = new SignUpCommand("테스터", TEST_PHONE, TEST_TOKEN, AuthPlatform.GOOGLE);

    // when
    signUpService.signUp(command);

    // then
    ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
    ArgumentCaptor<Activity> activityCaptor = ArgumentCaptor.forClass(Activity.class);

    verify(userRepository).save(any(User.class));
    verify(userActivityHistoryRepository).save(userCaptor.capture(), activityCaptor.capture());

    User capturedUser = userCaptor.getValue();
    Activity capturedActivity = activityCaptor.getValue();

    assertThat(capturedUser.getId()).isEqualTo(TEST_USER_ID);
    assertThat(capturedActivity.getGeneration()).isEqualTo(ACTIVITY_GENERATION);
  }
}
