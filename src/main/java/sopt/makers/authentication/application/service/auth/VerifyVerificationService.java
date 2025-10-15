package sopt.makers.authentication.application.service.auth;

import static sopt.makers.authentication.domain.auth.exception.AuthFailure.EXPIRED_PHONE_VERIFICATION;
import static sopt.makers.authentication.domain.auth.exception.AuthFailure.INVALID_PHONE_VERIFICATION_CODE;

import sopt.makers.authentication.adapter.out.external.oauth.MagicLoginProperty;
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
  private final MagicLoginProperty magicLoginProperty;

  @Override
  @Transactional
  public VerifyVerificationResult verify(VerifyVerificationCommand command) {
    if (isMagicVerification(command.phone(), command.code())) {
      return new VerifyVerificationResult(magicLoginProperty.name(), magicLoginProperty.phone());
    }

    PhoneVerification targetVerification =
        PhoneVerification.of(command.phone(), command.verificationType(), command.code());
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

    return new VerifyVerificationResult(
        verifiedVerification.getName(), verifiedVerification.getPhone());
  }

  private boolean isMagicVerification(String phone, String code) {
    if (!phone.equals(magicLoginProperty.phone())) {
      return false;
    }
    if (!code.equals(magicLoginProperty.code())) {
      throw new AuthException(INVALID_PHONE_VERIFICATION_CODE);
    }

    return true;
  }
}
