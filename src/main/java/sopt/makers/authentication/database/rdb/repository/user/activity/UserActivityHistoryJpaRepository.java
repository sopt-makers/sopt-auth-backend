package sopt.makers.authentication.database.rdb.repository.user.activity;

import sopt.makers.authentication.database.rdb.entity.UserActivityHistoryEntity;

import org.springframework.data.jpa.repository.JpaRepository;

interface UserActivityHistoryJpaRepository extends JpaRepository<UserActivityHistoryEntity, Long> {}
