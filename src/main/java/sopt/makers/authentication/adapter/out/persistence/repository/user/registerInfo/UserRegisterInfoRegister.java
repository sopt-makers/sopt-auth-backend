package sopt.makers.authentication.adapter.out.persistence.repository.user.registerInfo;

import sopt.makers.authentication.adapter.out.persistence.entity.UserRegisterInfoEntity;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegisterInfoRegister {
  private final UserRegisterInfoJpaRepository userRegisterInfoJpaRepository;

  public UserRegisterInfoEntity save(UserRegisterInfoEntity userRegisterInfoEntity) {
    return userRegisterInfoJpaRepository.save(userRegisterInfoEntity);
  }
}
