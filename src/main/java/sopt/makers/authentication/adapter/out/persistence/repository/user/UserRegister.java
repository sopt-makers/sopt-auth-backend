package sopt.makers.authentication.adapter.out.persistence.repository.user;

import sopt.makers.authentication.adapter.out.persistence.entity.UserEntity;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegister {
  private final UserJpaRepository userJpaRepository;

  public UserEntity save(UserEntity userEntity) {
    return userJpaRepository.save(userEntity);
  }
}
