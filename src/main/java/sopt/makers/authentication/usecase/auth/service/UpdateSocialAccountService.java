package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.auth.port.in.UpdateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateSocialAccountService implements UpdateSocialAccountUsecase {
  private final UserRepository userRepository;
  private final OAuthAuthenticator oAuthAuthenticator;

  @Override
  public boolean update(UpdateSocialAccountCommand command) {
    User user = userRepository.findByPhone(command.phone());
    Long userId = userRepository.findIdByUser(user);
    String authPlatformId =
        oAuthAuthenticator.getAuthPlatformId(command.authPlatform(), command.code());
    SocialAccount socialAccount = SocialAccount.of(authPlatformId, command.authPlatform());
    user.updateSocialAccount(socialAccount);
    userRepository.save(userId, user);
    return true;
  }
}
