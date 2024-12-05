package sopt.makers.authentication.database.rdb.repository;

import sopt.makers.authentication.database.rdb.entity.UserEntity;
import sopt.makers.authentication.domain.auth.AuthPlatform;

import java.util.Optional;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.*;

public interface UserJpaRepository extends JpaRepository<UserEntity, Long> {
  Optional<UserEntity> findByAuthPlatformTypeAndAuthPlatformId(
      AuthPlatform authPlatformType, String authPlatformId);

  Optional<Long> findIdByAuthPlatformTypeAndAuthPlatformId(
      AuthPlatform authPlatformType, String authPlatformId);

  Optional<UserEntity> findByPhone(String phone);
}
