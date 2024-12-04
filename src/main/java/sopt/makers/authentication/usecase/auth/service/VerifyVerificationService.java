package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.PhoneVerification;
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
    boolean isVerified = targetVerification.equals(findVerification);

    if (isVerified) {
      phoneVerificationRepository.deletedByPhoneVerification(findVerification);
    }
    return new VerifyVerificationResult(isVerified);
  }
}
