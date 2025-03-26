package sopt.makers.authentication.database.rdb.repository;

import static sopt.makers.authentication.support.code.domain.failure.AuthFailure.NOT_FOUND_USER_WITH_SOCIAL_ACCOUNT;
import static sopt.makers.authentication.support.code.domain.failure.UserFailure.NOT_FOUND_PHONE;

import sopt.makers.authentication.database.rdb.entity.UserEntity;
import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.exception.domain.AuthException;
import sopt.makers.authentication.support.exception.domain.UserException;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
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

  public Long findIdByUser(User user) {
    AuthPlatform authPlatformType = user.getSocialAccount().authPlatformType();
    String authPlatformId = user.getSocialAccount().authPlatformId();
    return userJpaRepository
        .findIdByAuthPlatformTypeAndAuthPlatformId(authPlatformType, authPlatformId)
        .orElseThrow(() -> new AuthException(NOT_FOUND_USER_WITH_SOCIAL_ACCOUNT));
  }

  public User findByPhone(String phone) {
    UserEntity userEntity =
        userJpaRepository.findByPhone(phone).orElseThrow(() -> new UserException(NOT_FOUND_PHONE));
    return userEntity.toDomain();
  }
}
