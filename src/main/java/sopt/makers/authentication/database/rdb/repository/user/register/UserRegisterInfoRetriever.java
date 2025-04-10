package sopt.makers.authentication.database.rdb.repository.user.register;

import sopt.makers.authentication.database.rdb.entity.UserRegisterInfoEntity;

import java.util.*;

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
