package sopt.makers.authentication.database.rdb.repository.user;

import sopt.makers.authentication.database.rdb.entity.UserEntity;
import sopt.makers.authentication.domain.auth.AuthPlatform;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
  @Query(
      "SELECT DISTINCT u "
          + "FROM UserEntity u "
          + "JOIN FETCH u.userActivityHistoryList "
          + "WHERE u.authPlatformType = :authPlatformType "
          + "AND u.authPlatformId = :authPlatformId")
  Optional<UserEntity> findByAuthPlatformTypeAndAuthPlatformId(
      @Param("authPlatformType") AuthPlatform authPlatformType,
      @Param("authPlatformId") String authPlatformId);

  Optional<UserEntity> findByPhone(String phone);

  boolean existsByPhone(String phone);

  @Query(
      "SELECT DISTINCT u FROM UserEntity u JOIN FETCH u.userActivityHistoryList WHERE u.id IN :userIds")
  List<UserEntity> findAllWithActivityHistoriesByIdIn(@Param("userIds") List<Long> userIds);
}
