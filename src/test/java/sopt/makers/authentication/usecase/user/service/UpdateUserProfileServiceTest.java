package sopt.makers.authentication.usecase.user.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.Role;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.validator.PhoneVerificationValidator;
import sopt.makers.authentication.usecase.user.port.in.UpdateUserProfileUsecase.SoptActivityCommand;
import sopt.makers.authentication.usecase.user.port.in.UpdateUserProfileUsecase.UserProfileCommand;
import sopt.makers.authentication.usecase.user.port.out.UserActivityHistoryRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class UpdateUserProfileServiceTest {
  @InjectMocks private UpdateUserProfileService updateUserProfileService;
  @Mock private UserRepository userRepository;
  @Mock private UserActivityHistoryRepository userActivityHistoryRepository;
  @Mock private PhoneVerificationValidator phoneVerificationValidator;

  private final String phone = "01012345678";
  private final String email = "test@example.com";
  private final String profileImage = "image.png";
  private final LocalDate birthday = LocalDate.of(2000, 1, 1);

  private User mockedUser;
  private Profile originalProfile;
  private Profile updatedProfile;

  @BeforeEach
  void setUpMocks() {
    mockedUser = mock(User.class);
    originalProfile = mock(Profile.class);
    updatedProfile = mock(Profile.class);

    when(mockedUser.getId()).thenReturn(1L);
    when(mockedUser.getProfile()).thenReturn(originalProfile);
    when(originalProfile.updateProfile(email, phone, birthday, profileImage))
        .thenReturn(updatedProfile);
    when(userRepository.findById(1L)).thenReturn(mockedUser);
  }

  @Test
  @DisplayName("유저 프로필(프로필 이미지, 번호, 이메일, 생년월일) 및 활동 정보가 정상적으로 수정된다.")
  void 유저_프로필_및_활동_정보_수정시_정상적으로_수정이_완료된다() {
    // given
    Activity existingActivity = Activity.of(1L, 33, Team.MEDIA, Part.SERVER, Role.MEMBER);
    ActivityList activityList = ActivityList.of(List.of(existingActivity));
    when(userActivityHistoryRepository.findByUser(1L)).thenReturn(activityList);

    SoptActivityCommand activityCommand = SoptActivityCommand.of(1L, "미디어팀");
    UserProfileCommand userProfileCommand =
        UserProfileCommand.of(1L, profileImage, birthday, phone, email, List.of(activityCommand));

    // when
    updateUserProfileService.updateUserProfile(userProfileCommand);

    // then
    verify(userRepository).update(mockedUser, updatedProfile);
    verify(userActivityHistoryRepository, times(1)).update(eq(mockedUser), any(ActivityList.class));
  }
}
