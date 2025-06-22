package sopt.makers.authentication.application.service.auth;

import sopt.makers.authentication.application.port.in.auth.UpdateSocialAccountUsecase;
import sopt.makers.authentication.application.port.out.auth.OAuthAuthenticator;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.application.validator.auth.PhoneVerificationValidator;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateSocialAccountService implements UpdateSocialAccountUsecase {
  private final UserRepository userRepository;
  private final OAuthAuthenticator oAuthAuthenticator;
  private final PhoneVerificationValidator phoneVerificationValidator;

  @Override
  public boolean update(UpdateSocialAccountCommand command) {
    User user = userRepository.findByPhone(command.phone());
    phoneVerificationValidator.validate(
        user.getProfile().name(), command.phone(), PhoneVerificationType.CHANGE_SOCIAL_PLATFORM);
    String authPlatformId =
        oAuthAuthenticator.getIdentifier(command.token(), command.authPlatform());
    SocialAccount updatedSocialAccount = SocialAccount.of(authPlatformId, command.authPlatform());
    userRepository.update(user, updatedSocialAccount);
    return true;
  }
}
