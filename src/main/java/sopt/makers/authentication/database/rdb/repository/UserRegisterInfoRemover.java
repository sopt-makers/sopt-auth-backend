package sopt.makers.authentication.database.rdb.repository;

import sopt.makers.authentication.database.rdb.entity.UserRegisterInfoEntity;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserRegisterInfoRemover {
  private final UserRegisterInfoJpaRepository jpaRepository;

  public void remove(final UserRegisterInfoEntity entity) {

    jpaRepository.delete(entity);
  }
}
