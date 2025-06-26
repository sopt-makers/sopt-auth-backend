package sopt.makers.authentication.adapter.out.persistence.repository.user.registerInfo;

import sopt.makers.authentication.adapter.out.persistence.entity.UserRegisterInfoEntity;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegisterInfoRemover {
  private final UserRegisterInfoJpaRepository jpaRepository;

  public void remove(final UserRegisterInfoEntity entity) {
    jpaRepository.delete(entity);
  }
}
