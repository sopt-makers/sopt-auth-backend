package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.validator.PhoneVerificationValidator;
import sopt.makers.authentication.usecase.auth.port.in.UpdateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;

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
