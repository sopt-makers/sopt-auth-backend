package sopt.makers.authentication.usecase.auth.service;

import static sopt.makers.authentication.domain.auth.PhoneVerificationType.REGISTER;
import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.ALREADY_REGISTER_PHONE_NUMBER;
import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.NOT_FOUND_REGISTER_INFO;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.message.Message;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.message.port.out.MessageSendPort;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;

import java.util.Optional;

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
      case REGISTER -> handleRegister(command.phone());
      case CHANGE, SEARCH -> handleChangeOrSearch(command.phone(), command.verificationType());
    };
  }

  private PhoneVerification handleRegister(String phone) {
    Optional<UserRegisterInfo> targetRegisterInfo = userRegisterInfoRepository.findByPhone(phone);

    if (targetRegisterInfo.isPresent()) {
      UserRegisterInfo registerInfo = targetRegisterInfo.get();
      return PhoneVerification.create(registerInfo.getName(), registerInfo.getPhone(), REGISTER);
    }

    boolean existUser = userRepository.existsByPhone(phone);
    if (existUser) {
      throw new AuthException(ALREADY_REGISTER_PHONE_NUMBER);
    }
    throw new AuthException(NOT_FOUND_REGISTER_INFO);
  }

  private PhoneVerification handleChangeOrSearch(String phone, PhoneVerificationType type) {
    User user = userRepository.findByPhone(phone);
    return PhoneVerification.create(user.getProfile().name(), user.getProfile().phone(), type);
  }

  private String convertCodeToMessage(String code) {
    return String.format(FORMAT_VERIFICATION_MESSAGE, code);
  }
}
