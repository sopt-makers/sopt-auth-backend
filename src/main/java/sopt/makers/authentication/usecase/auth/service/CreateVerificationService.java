package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.message.Message;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.message.port.out.MessageSendPort;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateVerificationService implements CreatePhoneVerificationUsecase {

  private static final String FORMAT_VERIFICATION_MESSAGE = "[SOPT makers]\n인증번호 [%s]를 입력해주세요.";

  private final UserRepository userRepository;
  private final UserRegisterInfoRepository userRegisterInfoRepository;
  private final PhoneVerificationRepository verificationRepository;
  private final MessageSendPort messageSendPort;

  @Override
  public PhoneVerification create(CreateVerificationCommand command) {
    PhoneVerification phoneVerification = createPhoneVerificationByCommand(command);

    Message verificationMessage =
        Message.sms(
            command.phone(),
            convertCodeToMessage(phoneVerification.getVerificationCode().getCode()));

    messageSendPort.sendMessage(verificationMessage);
    return verificationRepository.create(phoneVerification);
  }

  private PhoneVerification createPhoneVerificationByCommand(CreateVerificationCommand command) {
    return switch (command.verificationType()) {
      case REGISTER -> {
        UserRegisterInfo registerInfo = userRegisterInfoRepository.findByPhone(command.phone());
        yield PhoneVerification.create(
            registerInfo.getName(), registerInfo.getPhone(), command.verificationType());
      }
      case CHANGE, SEARCH -> {
        User user = userRepository.findByPhone(command.phone());
        yield PhoneVerification.create(
            user.getProfile().name(), user.getProfile().phone(), command.verificationType());
      }
    };
  }

  private String convertCodeToMessage(String code) {
    return String.format(FORMAT_VERIFICATION_MESSAGE, code);
  }
}
