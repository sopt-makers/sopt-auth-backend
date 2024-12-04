package sopt.makers.authentication.database.rdb.repository;

import sopt.makers.authentication.database.rdb.entity.UserEntity;
import sopt.makers.authentication.domain.user.User;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional
@RequiredArgsConstructor
public class UserRegister {
  private final UserJpaRepository userJpaRepository;

  public User save(Long id, User user) {
    UserEntity updatedUserEntity = UserEntity.fromDomain(id, user);
    userJpaRepository.save(updatedUserEntity);
    return updatedUserEntity.toDomain();
  }
}
