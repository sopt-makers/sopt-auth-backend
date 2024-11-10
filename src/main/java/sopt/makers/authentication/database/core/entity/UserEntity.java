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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class UserEntity extends BaseTimeEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull String name;
  @NotNull String phone;
  @NotNull String email;
  @NotNull LocalDate birthday;
  @NotNull String authPlatformId;

  @NotNull
  @Enumerated(EnumType.STRING)
  AuthPlatform authPlatformType;

  @NotNull
  @ColumnDefault(value = "false")
  Boolean isActive;

  public UserEntity(final User user, boolean isActive) {
    Profile profile = user.getProfile();
    SocialAccount socialAccount = user.getSocialAccount();

    this.name = profile.name();
    this.phone = profile.phone();
    this.email = profile.email();
    this.birthday = profile.birthday();
    this.authPlatformId = socialAccount.authPlatformId();
    this.authPlatformType = socialAccount.authPlatformType();
  }
}
