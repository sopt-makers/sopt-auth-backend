package sopt.makers.authentication.usecase.auth.port.in;

import sopt.makers.authentication.domain.auth.AuthPlatform;

public interface SignUpUsecase {

  void signUp(SignUpCommand command);

  record SignUpCommand(
      String name, String phone, String token, AuthPlatform authPlatform, String profileImage) {}
}
