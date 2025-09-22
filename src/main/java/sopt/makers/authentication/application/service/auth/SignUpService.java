package sopt.makers.authentication.application.service.auth;

import static sopt.makers.authentication.domain.auth.exception.AuthFailure.*;

import sopt.makers.authentication.adapter.out.external.app.AppClient;
import sopt.makers.authentication.adapter.out.external.exception.ClientException.AppRequestException;
import sopt.makers.authentication.adapter.out.external.exception.ClientException.AppResponseException;
import sopt.makers.authentication.adapter.out.external.exception.ClientException.PlaygroundRequestException;
import sopt.makers.authentication.adapter.out.external.exception.ClientException.PlaygroundResponseException;
import sopt.makers.authentication.adapter.out.external.oauth.MagicLoginProperty;
import sopt.makers.authentication.adapter.out.external.playground.PlaygroundClient;
import sopt.makers.authentication.application.port.in.auth.SignUpUsecase;
import sopt.makers.authentication.application.port.out.auth.OAuthAuthenticator;
import sopt.makers.authentication.application.port.out.user.UserActivityHistoryRepository;
import sopt.makers.authentication.application.port.out.user.UserRegisterInfoRepository;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.application.validator.auth.PhoneVerificationValidator;
import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.auth.exception.AuthException;
import sopt.makers.authentication.domain.auth.exception.AuthFailure;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SignUpService implements SignUpUsecase {
  private final OAuthAuthenticator oAuthAuthenticator;
  private final UserRepository userRepository;
  private final UserRegisterInfoRepository userRegisterInfoRepository;
  private final UserActivityHistoryRepository userActivityHistoryRepository;
  private final PhoneVerificationValidator phoneVerificationValidator;
  private final MagicLoginProperty magicLoginProperty;
  private final PlaygroundClient playgroundClient;
  private final AppClient appClient;

  @Transactional
  @Override
  public void signUp(SignUpCommand command) {
    if (isMagicPhone(command.phone())) {
      signUpForMagicNumber(command.token(), command.authPlatform());
    } else {
      signUpForSoptUser(command.token(), command.phone(), command.authPlatform());
    }
  }

  private void signUpForMagicNumber(String token, AuthPlatform authPlatform) {
    User user = userRepository.findByPhone(magicLoginProperty.phone());
    String authPlatformId = oAuthAuthenticator.getIdentifier(token, authPlatform);
    SocialAccount updatedSocialAccount = createSocialAccount(authPlatformId, authPlatform);
    User updatedUser = user.updateSocialAccount(updatedSocialAccount);
    userRepository.save(updatedUser);
  }

  private void signUpForSoptUser(String token, String phone, AuthPlatform authPlatform) {
    phoneVerificationValidator.validate(phone, PhoneVerificationType.REGISTER);
    String authPlatformId = oAuthAuthenticator.getIdentifier(token, authPlatform);
    UserRegisterInfo targetRegisterInfo =
        userRegisterInfoRepository
            .findByPhone(phone)
            .orElseThrow(() -> new AuthException(NOT_FOUND_REGISTER_INFO));
    SocialAccount socialAccount = createSocialAccount(authPlatformId, authPlatform);
    Profile profile = createProfile(targetRegisterInfo);
    Activity activity = createActivity(targetRegisterInfo);
    User newUser = User.createNewUser(socialAccount, profile);
    User savedUser = userRepository.save(newUser);

    try {
      userActivityHistoryRepository.save(savedUser, activity);
      userRegisterInfoRepository.delete(targetRegisterInfo);
      appClient.createMemberProfile(savedUser.getId());
      playgroundClient.createMemberProfile(savedUser.getId());
    } catch (AppRequestException | AppResponseException e) {
      // 케이스 1: App이 실패 → Playground는 요청 시도 x
      log.error("앱 유저 생성 요청 실패 userId={}", savedUser.getId());
      throw new AuthException(APP_SYNC_FAIL);
    } catch (PlaygroundRequestException | PlaygroundResponseException e) {
      // 케이스 2: App은 성공했지만 Playground 실패
      try {
        appClient.deleteMemberProfile(savedUser.getId());
      } catch (Exception ex) {
        log.error("앱 유저 Delete 요청 실패 userId={}", savedUser.getId());
      }
      throw new AuthException(PLAYGROUND_SYNC_FAIL);
    }
  }

  private SocialAccount createSocialAccount(String authPlatformId, AuthPlatform authPlatform) {
    return switch (authPlatform) {
      case GOOGLE -> SocialAccount.of(authPlatformId, AuthPlatform.GOOGLE);
      case APPLE -> SocialAccount.of(authPlatformId, AuthPlatform.APPLE);
      default -> throw new AuthException(AuthFailure.INVALID_SOCIAL_PLATFORM);
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

  private boolean isMagicPhone(String phone) {
    return (phone.equals(magicLoginProperty.phone()));
  }
}
