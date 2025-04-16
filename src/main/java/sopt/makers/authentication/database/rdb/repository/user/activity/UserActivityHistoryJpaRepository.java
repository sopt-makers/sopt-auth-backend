package sopt.makers.authentication.database.rdb.repository.user.activity;

import sopt.makers.authentication.database.rdb.entity.UserActivityHistoryEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserActivityHistoryJpaRepository extends JpaRepository<UserActivityHistoryEntity, Long> {
  List<UserActivityHistoryEntity> findByUserId(Long userId);
}
