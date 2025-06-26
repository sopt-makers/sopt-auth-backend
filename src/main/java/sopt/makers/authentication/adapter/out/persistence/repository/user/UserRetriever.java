package sopt.makers.authentication.adapter.out.persistence.repository.user;

import static sopt.makers.authentication.domain.auth.exception.AuthFailure.NOT_FOUND_USER_WITH_SOCIAL_ACCOUNT;
import static sopt.makers.authentication.domain.user.exception.UserFailure.NOT_FOUND_USER;

import sopt.makers.authentication.adapter.out.persistence.entity.UserEntity;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.auth.exception.AuthException;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.exception.UserException;

import java.util.List;

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

  public User findByPhone(String phone) {
    UserEntity userEntity =
        userJpaRepository.findByPhone(phone).orElseThrow(() -> new UserException(NOT_FOUND_USER));
    return userEntity.toDomain();
  }

  public UserEntity findById(Long userId) {
    return userJpaRepository.findById(userId).orElseThrow(() -> new UserException(NOT_FOUND_USER));
  }

  public List<User> findAllById(List<Long> userIds) {
    List<UserEntity> userEntityList = userJpaRepository.findAllWithActivityHistoriesByIdIn(userIds);
    return userEntityList.stream().map(UserEntity::toDomain).toList();
  }

  public boolean existsByPhone(String phone) {
    return userJpaRepository.existsByPhone(phone);
  }

  public List<User> findAllByActivity(Integer generation, Part part) {
    List<UserEntity> userEntityList =
        userJpaRepository.findAllWithActivityHistoriesByGenerationAndPart(generation, part);

    return userEntityList.stream().map(UserEntity::toDomain).toList();
  }

  public int countByGeneration(int generation) {
    return userJpaRepository.countByGeneration(generation);
  }
}
