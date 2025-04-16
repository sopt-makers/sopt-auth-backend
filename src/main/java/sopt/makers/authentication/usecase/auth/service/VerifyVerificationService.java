package sopt.makers.authentication.usecase.auth.service;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.INVALID_PHONE_VERIFICATION_CODE;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerifyVerificationService implements VerifyPhoneVerificationUsecase {
  private final PhoneVerificationRepository phoneVerificationRepository;

  @Override
  public VerifyVerificationResult verify(VerifyVerificationCommand command) {
    PhoneVerification targetVerification =
        PhoneVerification.of(
            command.name(), command.phone(), command.verificationType(), command.code());
    PhoneVerification findVerification =
        phoneVerificationRepository.findByPhoneVerification(targetVerification);
    boolean isNotVerified =
        !targetVerification.getVerificationCode().equals(findVerification.getVerificationCode());

    if (isNotVerified) {
      throw new AuthException(INVALID_PHONE_VERIFICATION_CODE);
    }
    phoneVerificationRepository.deleteByPhoneVerification(findVerification);
    return new VerifyVerificationResult(command.name(), command.phone());
  }
}
