package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.usecase.auth.port.in.SignUpUsecase;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SignUpService implements SignUpUsecase {
  private final OAuthAuthenticator oAuthAuthenticator;

  private final UserRepository userRepository;
  private final UserRegisterInfoRepository userRegisterInfoRepository;

  @Override
  public void signUp(SignUpCommand command) {
    String identifier = oAuthAuthenticator.getIdentifier(command.token(), command.authPlatform());
    UserRegisterInfo registerInfo = userRegisterInfoRepository.findByPhone(command.phone());

    SocialAccount socialAccount = createSocialAccount(command.authPlatform(), identifier);
    Profile profile = createProfile(registerInfo);
    User newUser = User.createNewUser(socialAccount, profile);

    userRepository.save(newUser);
    userRegisterInfoRepository.delete(registerInfo);
  }

  private SocialAccount createSocialAccount(AuthPlatform authPlatform, String identifier) {
    return switch (authPlatform) {
      case GOOGLE -> SocialAccount.of(identifier, AuthPlatform.GOOGLE);
      case APPLE -> SocialAccount.of(identifier, AuthPlatform.APPLE);
    };
  }

  private Profile createProfile(UserRegisterInfo registerInfo) {
    return Profile.of(
        registerInfo.getName(),
        registerInfo.getEmail(),
        registerInfo.getPhone(),
        registerInfo.getBirthday());
  }
}
