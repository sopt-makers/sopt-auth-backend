package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationCreatedEvent;
import sopt.makers.authentication.domain.message.MessageType;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserRegisterInfo;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateVerificationService implements CreatePhoneVerificationUsecase {

  private final UserRepository userRepository;
  private final UserRegisterInfoRepository userRegisterInfoRepository;
  private final PhoneVerificationRepository verificationRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  public PhoneVerification create(CreateVerificationCommand command) {
    PhoneVerification phoneVerification = createPhoneVerificationByCommand(command);

    PhoneVerification savedPhoneVerification = verificationRepository.create(phoneVerification);

    eventPublisher.publishEvent(
        new PhoneVerificationCreatedEvent(
            savedPhoneVerification.getPhone(),
            savedPhoneVerification.getVerificationCode().getCode(),
            MessageType.SMS));
    return savedPhoneVerification;
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
}
