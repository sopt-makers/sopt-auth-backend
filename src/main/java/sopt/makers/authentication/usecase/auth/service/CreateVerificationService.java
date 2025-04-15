package sopt.makers.authentication.usecase.auth.service;

import static sopt.makers.authentication.domain.auth.PhoneVerificationType.REGISTER;
import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.ALREADY_REGISTER_PHONE_NUMBER;
import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.NOT_FOUND_REGISTER_INFO;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationCreatedEvent;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.message.MessageType;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.usecase.auth.port.in.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRegisterInfoRepository;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateVerificationService implements CreatePhoneVerificationUsecase {
  private static final String FORMAT_VERIFICATION_MESSAGE = "[SOPT makers]\n인증번호 [%s]를 입력해주세요.";
  private final UserRepository userRepository;
  private final UserRegisterInfoRepository userRegisterInfoRepository;
  private final PhoneVerificationRepository verificationRepository;
  private final ApplicationEventPublisher eventPublisher;

  @Override
  @Transactional
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
      case REGISTER -> handleRegister(command.phone());
      case CHANGE, SEARCH -> handleChangeOrSearch(command.phone(), command.verificationType());
    };
  }

  private PhoneVerification handleRegister(String phone) {
    return userRegisterInfoRepository
        .findByPhone(phone)
        .map(info -> PhoneVerification.create(info.getName(), info.getPhone(), REGISTER))
        .orElseThrow(
            () -> {
              boolean existUser = userRepository.existsByPhone(phone);
              if (existUser) {
                return new AuthException(ALREADY_REGISTER_PHONE_NUMBER);
              }
              return new AuthException(NOT_FOUND_REGISTER_INFO);
            });
  }

  private PhoneVerification handleChangeOrSearch(String phone, PhoneVerificationType type) {
    User user = userRepository.findByPhone(phone);
    return PhoneVerification.create(user.getProfile().name(), user.getProfile().phone(), type);
  }

  private String convertCodeToMessage(String code) {
    return String.format(FORMAT_VERIFICATION_MESSAGE, code);
  }
}
