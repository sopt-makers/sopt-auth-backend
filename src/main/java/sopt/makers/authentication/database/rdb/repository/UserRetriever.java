package sopt.makers.authentication.database.rdb.repository;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.NOT_FOUND_USER_WITH_SOCIAL_ACCOUNT;

import sopt.makers.authentication.database.rdb.entity.UserEntity;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.exception.domain.AuthException;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Component
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserRetriever {
  private final UserJpaRepository userJpaRepository;

  public User findBySocialAccount(SocialAccount socialAccount) {
    UserEntity userEntity =
        userJpaRepository
            .findByAuthPlatformTypeAndAuthPlatformId(
                socialAccount.authPlatformType(), socialAccount.authPlatformId())
            .orElseThrow(() -> new AuthException(NOT_FOUND_USER_WITH_SOCIAL_ACCOUNT));
    return userEntity.toDomain();
  }
}
