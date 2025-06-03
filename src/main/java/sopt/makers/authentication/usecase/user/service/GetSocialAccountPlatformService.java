package sopt.makers.authentication.usecase.user.service;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.validator.PhoneVerificationValidator;
import sopt.makers.authentication.usecase.auth.port.in.GetSocialAccountUsecase;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetSocialAccountPlatformService implements GetSocialAccountUsecase {
  private final UserRepository userRepository;
  private final PhoneVerificationValidator phoneVerificationValidator;

  @Override
  public SocialAccountPlatformInfo getSocialAccountPlatform(
      GetSocialAccountPlatformCommand command) {
    User user = userRepository.findByPhone(command.phone());
    phoneVerificationValidator.validate(
        user.getProfile().name(), command.phone(), PhoneVerificationType.SEARCH_SOCIAL_PLATFORM);
    AuthPlatform authPlatform = user.getSocialAccount().authPlatformType();
    return new SocialAccountPlatformInfo(authPlatform.name());
  }
}
