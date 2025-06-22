package sopt.makers.authentication.application.service.auth;

import static sopt.makers.authentication.domain.auth.exception.AuthFailure.EXPIRED_PHONE_VERIFICATION;
import static sopt.makers.authentication.domain.auth.exception.AuthFailure.INVALID_PHONE_VERIFICATION_CODE;

import sopt.makers.authentication.application.port.in.auth.VerifyPhoneVerificationUsecase;
import sopt.makers.authentication.application.port.out.auth.PhoneVerificationRepository;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.exception.AuthException;

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
      phoneVerificationRepository.deleteByPhoneVerification(findVerification);
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
