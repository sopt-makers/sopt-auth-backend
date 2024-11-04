package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.usecase.auth.port.in.CreateVerificationUsecase;
import sopt.makers.authentication.usecase.auth.port.out.VerificationRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CreateVerificationService implements CreateVerificationUsecase {
  private final VerificationRepository verificationRepository;
}
