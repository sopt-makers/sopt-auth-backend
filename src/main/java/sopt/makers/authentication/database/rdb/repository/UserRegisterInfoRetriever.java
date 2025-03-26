package sopt.makers.authentication.database.rdb.repository;

import sopt.makers.authentication.database.rdb.entity.UserRegisterInfoEntity;
import sopt.makers.authentication.support.code.domain.failure.UserFailure;
import sopt.makers.authentication.support.exception.domain.UserException;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRegisterInfoRetriever {
  private final UserRegisterInfoJpaRepository jpaRepository;

  public UserRegisterInfoEntity findByPhone(String phone) {
    return jpaRepository
        .findByPhone(phone)
        .orElseThrow(() -> new UserException(UserFailure.NOT_FOUND_REGISTER_INFO));
  }
}
