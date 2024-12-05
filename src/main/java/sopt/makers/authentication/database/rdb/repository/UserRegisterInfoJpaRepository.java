package sopt.makers.authentication.database.rdb.repository;

import sopt.makers.authentication.database.rdb.entity.UserRegisterInfoEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRegisterInfoJpaRepository extends JpaRepository<UserRegisterInfoEntity, Long> {

  Optional<UserRegisterInfoEntity> findByPhone(String phone);
}
