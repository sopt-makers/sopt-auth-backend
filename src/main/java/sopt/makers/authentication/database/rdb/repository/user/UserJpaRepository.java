package sopt.makers.authentication.database.rdb.repository.user;

import sopt.makers.authentication.database.rdb.entity.UserEntity;
import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.user.Part;

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

  @Query(
      """
            SELECT DISTINCT u
            FROM UserEntity u
            JOIN FETCH u.userActivityHistoryList a
            WHERE (:generation IS NULL OR a.generation = :generation)
            AND (:part IS NULL OR a.part = :part)
            """)
  List<UserEntity> findAllWithActivityHistoriesByGenerationAndPart(
      @Param("generation") Integer generation, @Param("part") Part part);

  @Query(
      """
        SELECT COUNT(DISTINCT u.id)
        FROM UserEntity u
        JOIN u.userActivityHistoryList a
        WHERE a.generation = :generation
    """)
  int countByGeneration(@Param("generation") int generation);
}
