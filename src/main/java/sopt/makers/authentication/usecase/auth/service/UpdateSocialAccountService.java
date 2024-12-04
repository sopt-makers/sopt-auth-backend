package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.auth.port.in.UpdateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateSocialAccountService implements UpdateSocialAccountUsecase {
  private final UserRepository userRepository;

  @Override
  public boolean update(UpdateSocialAccountCommand command) {
    User user = userRepository.findByPhone(command.phone());
    Long userId = userRepository.findIdByUser(user);
    SocialAccount socialAccount = SocialAccount.of(command.code(), command.authPlatform());
    user.updateSocialAccount(socialAccount);
    userRepository.save(userId, user);
    return true;
  }
}
