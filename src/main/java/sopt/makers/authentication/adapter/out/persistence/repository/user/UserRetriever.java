package sopt.makers.authentication.adapter.out.persistence.repository.user;

import static sopt.makers.authentication.domain.auth.exception.AuthFailure.NOT_FOUND_USER_WITH_SOCIAL_ACCOUNT;
import static sopt.makers.authentication.domain.user.exception.UserFailure.NOT_FOUND_USER;

import sopt.makers.authentication.adapter.out.persistence.entity.UserEntity;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.auth.exception.AuthException;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserOrderBy;
import sopt.makers.authentication.domain.user.exception.UserException;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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

  public UserEntity findByIdWithHistories(Long userId) {
    return userJpaRepository
        .findWithActivityHistoriesById(userId)
        .orElseThrow(() -> new UserException(NOT_FOUND_USER));
  }

  public List<User> findAllById(List<Long> userIds) {
    List<UserEntity> userEntityList = userJpaRepository.findAllWithActivityHistoriesByIdIn(userIds);
    return userEntityList.stream().map(UserEntity::toDomain).toList();
  }

  public boolean existsByPhone(String phone) {
    return userJpaRepository.existsByPhone(phone);
  }

  public Page<User> findAllByGenerationAndPartAndNameAndTeam(
      Integer generation,
      Part part,
      String name,
      Team team,
      Boolean isAdmin,
      Pageable pageable,
      UserOrderBy orderBy) {
    if (orderBy.isGenerationOrder()) {
      return findByGenerationOrder(generation, part, name, team, isAdmin, pageable, orderBy);
    }
    return userJpaRepository
        .findAllWithActivityHistoriesByGenerationAndPartAndNameAndTeam(
            generation, part, name, team, isAdmin, pageable)
        .map(UserEntity::toDomain);
  }

  private Page<User> findByGenerationOrder(
      Integer generation,
      Part part,
      String name,
      Team team,
      Boolean isAdmin,
      Pageable pageable,
      UserOrderBy orderBy) {
    Page<Long> userIdPage =
        orderBy == UserOrderBy.LATEST_GENERATION
            ? userJpaRepository.findUserIdsOrderByMatchedGenerationDesc(
                generation, part, name, team, isAdmin, pageable)
            : userJpaRepository.findUserIdsOrderByMatchedGenerationAsc(
                generation, part, name, team, isAdmin, pageable);

    List<Long> orderedIds = userIdPage.getContent();
    if (orderedIds.isEmpty()) {
      return userIdPage.map(id -> null);
    }

    Map<Long, UserEntity> userById =
        userJpaRepository.findAllWithActivityHistoriesByIdIn(orderedIds).stream()
            .collect(Collectors.toMap(UserEntity::getId, Function.identity()));

    List<User> orderedUsers =
        orderedIds.stream()
            .map(userById::get)
            .filter(Objects::nonNull)
            .map(UserEntity::toDomain)
            .toList();

    return new PageImpl<>(orderedUsers, pageable, userIdPage.getTotalElements());
  }

  public int countByGenerationAndIsSopt(int generation, boolean isSopt) {
    return userJpaRepository.countByGenerationAndIsSopt(generation, isSopt);
  }
}
