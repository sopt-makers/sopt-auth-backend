package sopt.makers.authentication.application.service.auth;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.NOT_FOUND_REGISTER_INFO;

import sopt.makers.authentication.application.port.in.auth.SignUpUsecase;
import sopt.makers.authentication.application.port.out.auth.OAuthAuthenticator;
import sopt.makers.authentication.application.port.out.user.UserActivityHistoryRepository;
import sopt.makers.authentication.application.port.out.user.UserRegisterInfoRepository;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.support.validator.PhoneVerificationValidator;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUsecase {
  private final OAuthAuthenticator oAuthAuthenticator;
  private final UserRepository userRepository;
  private final UserRegisterInfoRepository userRegisterInfoRepository;
  private final UserActivityHistoryRepository userActivityHistoryRepository;
  private final PhoneVerificationValidator phoneVerificationValidator;

  @Transactional
  @Override
  public void signUp(SignUpCommand command) {
    phoneVerificationValidator.validate(
        command.name(), command.phone(), PhoneVerificationType.REGISTER);
    String authPlatformId =
        oAuthAuthenticator.getIdentifier(command.token(), command.authPlatform());
    UserRegisterInfo targetRegisterInfo =
        userRegisterInfoRepository
            .findByPhone(command.phone())
            .orElseThrow(() -> new AuthException(NOT_FOUND_REGISTER_INFO));
    SocialAccount socialAccount = createSocialAccount(authPlatformId, command.authPlatform());
    Profile profile = createProfile(targetRegisterInfo);
    Activity activity = createActivity(targetRegisterInfo);
    User newUser = User.createNewUser(socialAccount, profile);
    User savedUser = userRepository.save(newUser);

    userActivityHistoryRepository.save(savedUser, activity);
    userRegisterInfoRepository.delete(targetRegisterInfo);
  }

  private SocialAccount createSocialAccount(String authPlatformId, AuthPlatform authPlatform) {
    return switch (authPlatform) {
      case GOOGLE -> SocialAccount.of(authPlatformId, AuthPlatform.GOOGLE);
      case APPLE -> SocialAccount.of(authPlatformId, AuthPlatform.APPLE);
    };
  }

  private Profile createProfile(UserRegisterInfo registerInfo) {
    return Profile.of(
        registerInfo.getName(),
        registerInfo.getEmail(),
        registerInfo.getPhone(),
        registerInfo.getBirthday());
  }

  private Activity createActivity(UserRegisterInfo registerInfo) {
    return Activity.of(registerInfo.getGeneration(), null, registerInfo.getPart());
  }
}
