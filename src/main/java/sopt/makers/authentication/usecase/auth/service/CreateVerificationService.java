package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.message.Message;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;
import sopt.makers.authentication.usecase.message.port.out.MessageSendPort;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateVerificationService implements CreatePhoneVerificationUsecase {

  private static final String FORMAT_VERIFICATION_MESSAGE = "[SOPT makers]\n인증번호 [%s]를 입력해주세요.";

  private final PhoneVerificationRepository verificationRepository;
  private final MessageSendPort messageSendPort;

  @Override
  public PhoneVerification create(CreateVerificationCommand command) {
    PhoneVerification phoneVerification =
        PhoneVerification.create(command.name(), command.phone(), command.verificationType());
    Message verificationMessage =
        Message.sms(
            command.phone(),
            convertCodeToMessage(phoneVerification.getVerificationCode().getCode()));

    messageSendPort.sendMessage(verificationMessage);
    return verificationRepository.save(phoneVerification);
  }

  private String convertCodeToMessage(String code) {
    return String.format(FORMAT_VERIFICATION_MESSAGE, code);
  }
}
