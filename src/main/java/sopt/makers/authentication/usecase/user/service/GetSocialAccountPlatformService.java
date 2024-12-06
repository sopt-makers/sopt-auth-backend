package sopt.makers.authentication.usecase.user.service;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.user.port.in.GetSocialAccountPlatform;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetSocialAccountPlatformService implements GetSocialAccountPlatform {
  private final UserRepository userRepository;

  @Override
  public SocialAccountPlatformInfo getSocialAccountPlatform(
      GetSocialAccountPlatformCommand command) {
    User user = userRepository.findByPhone(command.phone());
    AuthPlatform authPlatform = user.getSocialAccount().authPlatformType();
    return new SocialAccountPlatformInfo(authPlatform.name());
  }
}
