package sopt.makers.authentication.adapter.out.persistence.repository.user.register;

import sopt.makers.authentication.adapter.out.persistence.entity.UserRegisterInfoEntity;

import java.util.Optional;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegisterInfoRetriever {
  private final UserRegisterInfoJpaRepository jpaRepository;

  public Optional<UserRegisterInfoEntity> findByPhone(String phone) {
    return jpaRepository.findByPhone(phone);
  }
}
