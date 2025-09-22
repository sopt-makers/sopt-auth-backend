package sopt.makers.authentication.adapter.out.persistence.repository.user.activity;

import sopt.makers.authentication.adapter.out.persistence.entity.UserActivityHistoryEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserActivityHistoryJpaRepository extends JpaRepository<UserActivityHistoryEntity, Long> {
  List<UserActivityHistoryEntity> findByUserId(Long userId);

  List<UserActivityHistoryEntity> findAllByUserIdIn(List<Long> userIds);

  void deleteByUserId(Long userId);
}
