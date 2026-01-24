package sopt.makers.authentication.adapter.out.persistence.repository.user;

import sopt.makers.authentication.adapter.out.persistence.entity.UserEntity;
import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Team;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

  @Query("SELECT u FROM UserEntity u JOIN FETCH u.userActivityHistoryList WHERE u.id = :userId")
  Optional<UserEntity> findWithActivityHistoriesById(@Param("userId") Long userId);

  @Query(
      "SELECT DISTINCT u "
          + "FROM UserEntity u "
          + "JOIN FETCH u.userActivityHistoryList a "
          + "WHERE (:generation IS NULL OR a.generation = :generation) "
          + "AND (:part IS NULL OR a.part = :part) "
          + "AND (:name IS NULL OR u.name LIKE %:name%) "
          + "AND (:team IS NULL OR a.team = :team)"
          + "AND (:isAdmin IS NULL OR :isAdmin = FALSE OR a.role <> 'MEMBER')")
  Page<UserEntity> findAllWithActivityHistoriesByGenerationAndPartAndNameAndTeam(
      @Param("generation") Integer generation,
      @Param("part") Part part,
      @Param("name") String name,
      @Param("team") Team team,
      @Param("isAdmin") Boolean isAdmin,
      Pageable pageable);

  @Query(
      "SELECT COUNT(DISTINCT u.id) "
          + "FROM UserEntity u "
          + "JOIN u.userActivityHistoryList a "
          + "WHERE a.generation = :generation "
          + "AND a.isSopt = :isSopt")
  int countByGenerationAndIsSopt(
      @Param("generation") int generation, @Param("isSopt") boolean isSopt);

  void deleteById(Long userId);
}
