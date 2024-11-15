package sopt.makers.authentication.database.rdb.repository;

import sopt.makers.authentication.database.rdb.entity.PhoneVerificationEntity;

import org.springframework.data.jpa.repository.JpaRepository;

interface PhoneVerificationJpaRepository extends JpaRepository<PhoneVerificationEntity, Long> {}
