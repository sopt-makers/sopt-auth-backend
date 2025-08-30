package sopt.makers.authentication.application.service.auth;

import static sopt.makers.authentication.domain.auth.PhoneVerificationType.REGISTER;
import static sopt.makers.authentication.domain.auth.exception.AuthFailure.ALREADY_REGISTER_PHONE_NUMBER;
import static sopt.makers.authentication.domain.auth.exception.AuthFailure.NOT_FOUND_REGISTER_INFO;

import sopt.makers.authentication.adapter.out.external.oauth.MagicLoginProperty;
import sopt.makers.authentication.application.port.in.auth.CreatePhoneVerificationUsecase;
import sopt.makers.authentication.application.port.out.auth.PhoneVerificationRepository;
import sopt.makers.authentication.application.port.out.user.UserRegisterInfoRepository;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationCreatedEvent;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;
import sopt.makers.authentication.domain.auth.exception.AuthException;
import sopt.makers.authentication.domain.message.MessageType;
import sopt.makers.authentication.domain.user.User;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateVerificationService implements CreatePhoneVerificationUsecase {
  private static final String FORMAT_VERIFICATION_MESSAGE = "[SOPT makers]\n인증번호 [%s]를 입력해주세요.";
  private final UserRepository userRepository;
  private final UserRegisterInfoRepository userRegisterInfoRepository;
  private final PhoneVerificationRepository verificationRepository;
  private final ApplicationEventPublisher eventPublisher;
  private final MagicLoginProperty magicLoginProperty;

  @Override
  @Transactional
  public void create(CreateVerificationCommand command) {
    if (isMagicPhone(command.phone())) {
      return;
    }

    PhoneVerification phoneVerification = createPhoneVerificationByCommand(command);
    PhoneVerification savedPhoneVerification = verificationRepository.create(phoneVerification);
    String content = convertCodeToMessage(savedPhoneVerification.getVerificationCode().getCode());

    eventPublisher.publishEvent(
        new PhoneVerificationCreatedEvent(
            savedPhoneVerification.getPhone(), content, MessageType.SMS));
  }

  private boolean isMagicPhone(String phone) {
    return (phone.equals(magicLoginProperty.phone()));
  }

  private PhoneVerification createPhoneVerificationByCommand(CreateVerificationCommand command) {
    return switch (command.verificationType()) {
      case REGISTER -> handleRegister(command.phone());
      case CHANGE_SOCIAL_PLATFORM, SEARCH_SOCIAL_PLATFORM -> handleSocialPlatform(
          command.phone(), command.verificationType());
      case CHANGE_PHONE_NUMBER -> handlePhoneNumber(
          command.userId(), command.phone(), command.verificationType());
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

  private PhoneVerification handleSocialPlatform(String phone, PhoneVerificationType type) {
    User user = userRepository.findByPhone(phone);
    return PhoneVerification.create(user.getProfile().name(), user.getProfile().phone(), type);
  }

  private PhoneVerification handlePhoneNumber(
      Long userId, String phone, PhoneVerificationType type) {
    User user = userRepository.findById(userId);
    return PhoneVerification.create(user.getProfile().name(), phone, type);
  }

  private String convertCodeToMessage(String code) {
    return String.format(FORMAT_VERIFICATION_MESSAGE, code);
  }
}
