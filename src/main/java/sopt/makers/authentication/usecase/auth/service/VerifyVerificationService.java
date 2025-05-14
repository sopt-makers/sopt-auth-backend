package sopt.makers.authentication.usecase.auth.service;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.EXPIRED_PHONE_VERIFICATION;
import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.INVALID_PHONE_VERIFICATION_CODE;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.usecase.auth.port.in.VerifyPhoneVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.PhoneVerificationRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VerifyVerificationService implements VerifyPhoneVerificationUsecase {
  private final PhoneVerificationRepository phoneVerificationRepository;

  @Override
  @Transactional
  public VerifyVerificationResult verify(VerifyVerificationCommand command) {
    PhoneVerification targetVerification =
        PhoneVerification.of(
            command.name(), command.phone(), command.verificationType(), command.code());
    PhoneVerification findVerification =
        phoneVerificationRepository.findByPhoneVerification(targetVerification);

    if (findVerification.isExpired()) {
      throw new AuthException(EXPIRED_PHONE_VERIFICATION);
    }

    boolean isNotVerified =
        !targetVerification.getVerificationCode().equals(findVerification.getVerificationCode());

    if (isNotVerified) {
      throw new AuthException(INVALID_PHONE_VERIFICATION_CODE);
    }

    PhoneVerification verifiedVerification = findVerification.updateIsVerified();
    phoneVerificationRepository.update(verifiedVerification);

    return new VerifyVerificationResult(command.name(), command.phone());
  }
}
