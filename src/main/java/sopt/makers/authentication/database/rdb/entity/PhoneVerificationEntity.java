package sopt.makers.authentication.database.rdb.entity;

import sopt.makers.authentication.database.BaseEntity;
import sopt.makers.authentication.domain.auth.PhoneVerification;
import sopt.makers.authentication.domain.auth.PhoneVerificationType;

import jakarta.persistence.*;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "phone_verifications")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PhoneVerificationEntity extends BaseEntity {

  @Column(name = "name", nullable = false)
  String name;

  @Column(name = "phone", nullable = false)
  String phone;

  @Column(name = "code", nullable = false)
  String code;

  @Column(name = "type", nullable = false)
  @Enumerated(EnumType.STRING)
  PhoneVerificationType type;

  private PhoneVerificationEntity(PhoneVerification verification) {
    super();
    this.name = verification.getName();
    this.phone = verification.getPhone();
    this.code = verification.getVerificationCode().getCode();
    this.type = verification.getVerificationType();
  }

  public static PhoneVerificationEntity from(PhoneVerification phoneVerification) {
    PhoneVerificationEntity phoneVerificationEntity =
        new PhoneVerificationEntity(phoneVerification);
    phoneVerificationEntity.setId(phoneVerification.getId());
    return phoneVerificationEntity;
  }

  public PhoneVerification toDomain() {
    return PhoneVerification.of(this.getId(), this.name, this.phone, this.type, this.code);
  }
}
