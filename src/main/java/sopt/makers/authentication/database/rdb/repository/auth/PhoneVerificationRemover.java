package sopt.makers.authentication.database.rdb.repository.auth;

import sopt.makers.authentication.domain.auth.PhoneVerification;

import jakarta.transaction.Transactional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class PhoneVerificationRemover {

  private final PhoneVerificationJpaRepository jpaRepository;

  public void remove(PhoneVerification phoneVerification) {
    jpaRepository.deleteByVerification(phoneVerification);
  }
}
