package sopt.makers.authentication.database.postgres.repository;

import sopt.makers.authentication.database.postgres.entity.PhoneVerificationEntity;

import org.springframework.data.jpa.repository.JpaRepository;

interface PhoneVerificationJpaRepository extends JpaRepository<PhoneVerificationEntity, Long> {}
