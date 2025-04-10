package sopt.makers.authentication.usecase.auth.service;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.NOT_FOUND_REGISTER_INFO;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.usecase.auth.port.in.SignUpUsecase;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.user.port.out.UserActivityHistoryRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;

import java.util.Optional;

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

  @Transactional
  @Override
  public void signUp(SignUpCommand command) {
    String authPlatformId =
        oAuthAuthenticator.getIdentifier(command.token(), command.authPlatform());
    Optional<UserRegisterInfo> targetRegisterInfo =
        userRegisterInfoRepository.findByPhone(command.phone());

    if (targetRegisterInfo.isEmpty()) {
      throw new AuthException(NOT_FOUND_REGISTER_INFO);
    }

    UserRegisterInfo registerInfo = targetRegisterInfo.get();
    SocialAccount socialAccount = createSocialAccount(authPlatformId, command.authPlatform());
    Profile profile = createProfile(registerInfo);
    Activity activity = createActivity(registerInfo);
    User newUser = User.createNewUser(socialAccount, profile);
    User savedUser = userRepository.save(newUser);

    userActivityHistoryRepository.save(savedUser, activity);
    userRegisterInfoRepository.delete(registerInfo);
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
