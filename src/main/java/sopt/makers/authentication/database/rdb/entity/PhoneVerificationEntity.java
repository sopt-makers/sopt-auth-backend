package sopt.makers.authentication.database.rdb.entity;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PROTECTED;

import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phone_verifications")
@NoArgsConstructor(access = PROTECTED)
@AllArgsConstructor(access = PRIVATE)
public class PhoneVerificationEntity extends BaseEntity {

  @Column(name = "name")
  private String name;

  @Column(name = "phone", nullable = false)
  private String phone;

  @Column(name = "code", nullable = false)
  private String code;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  private PhoneVerificationType type;

  @Column(name = "is_verified", nullable = false)
  private boolean isVerified;

  private PhoneVerificationEntity(PhoneVerification verification) {
    super();
    this.name = verification.getName();
    this.phone = verification.getPhone();
    this.code = verification.getVerificationCode().getCode();
    this.type = verification.getVerificationType();
    this.isVerified = verification.isVerified();
  }

  public static PhoneVerificationEntity fromDomain(PhoneVerification phoneVerification) {
    PhoneVerificationEntity phoneVerificationEntity =
        new PhoneVerificationEntity(phoneVerification);
    if (phoneVerification.getId() != null) {
      phoneVerificationEntity.setId(phoneVerification.getId());
    }
    return phoneVerificationEntity;
  }

  public PhoneVerification toDomain() {
    return PhoneVerification.of(
        this.getId(),
        this.name,
        this.phone,
        this.type,
        this.code,
        this.getCreatedAt(),
        this.isVerified);
  }
}
