package sopt.makers.authentication.database.core.entity;

import sopt.makers.authentication.database.core.common.BaseTimeEntity;
import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.Profile;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.auth.User;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.*;

import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull String name;
  @NotNull String phone;
  String email;
  LocalDate birthday;
  @NotNull String authPlatformId;

  @NotNull
  @Enumerated(EnumType.STRING)
  AuthPlatform authPlatformType;

  @NotNull
  @ColumnDefault(value = "false")
  Boolean isActive;

  private UserEntity(
      long id,
      String name,
      String phone,
      String email,
      LocalDate birthday,
      String authPlatformId,
      AuthPlatform authPlatformType) {
    this.id = id;
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.birthday = birthday;
    this.authPlatformId = authPlatformId;
    this.authPlatformType = authPlatformType;
  }

  public static UserEntity fromDomain(final User user) {
    Profile profile = user.getProfile();
    SocialAccount socialAccount = user.getSocialAccount();
    return new UserEntity(
        user.getId(),
        profile.name(),
        profile.phone(),
        profile.email().orElse(null),
        profile.birthday(),
        socialAccount.authPlatformId(),
        socialAccount.authPlatformType());
  }

  public User toDomain() {
    SocialAccount socialAccount = SocialAccount.of(authPlatformId, authPlatformType.name());
    Profile profile = Profile.of(name, email, phone, birthday);
    return User.createNewUser(id, socialAccount, profile);
  }
}
