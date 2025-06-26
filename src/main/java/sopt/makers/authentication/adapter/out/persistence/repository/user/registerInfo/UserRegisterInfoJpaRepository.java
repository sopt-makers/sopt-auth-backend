package sopt.makers.authentication.adapter.out.persistence.repository.user.registerInfo;

import sopt.makers.authentication.adapter.out.persistence.entity.UserRegisterInfoEntity;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserRegisterInfoJpaRepository extends JpaRepository<UserRegisterInfoEntity, Long> {

  Optional<UserRegisterInfoEntity> findByPhone(String phone);
}
