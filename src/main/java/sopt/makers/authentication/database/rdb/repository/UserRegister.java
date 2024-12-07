package sopt.makers.authentication.database.rdb.repository;

import sopt.makers.authentication.database.rdb.entity.UserEntity;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserRegister {
  private final UserJpaRepository userJpaRepository;

  public UserEntity save(UserEntity userEntity) {
    return userJpaRepository.save(userEntity);
  }
}
