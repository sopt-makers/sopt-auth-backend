package sopt.makers.authentication.database.rdb.entity;

import static lombok.AccessLevel.PROTECTED;

import sopt.makers.authentication.database.BaseEntity;
import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;

import org.hibernate.annotations.ColumnDefault;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(name = "users")
public class UserEntity extends BaseEntity {

  @NotNull String name;
  @NotNull String phone;
  String email;
  LocalDate birthday;
  @NotNull String authPlatformId;
  String profileImage;

  @NotNull
  @Enumerated(EnumType.STRING)
  AuthPlatform authPlatformType;

  @NotNull
  @ColumnDefault(value = "false")
  Boolean isActive;

  private UserEntity(
      String name,
      String phone,
      String email,
      LocalDate birthday,
      String profileImage,
      String authPlatformId,
      AuthPlatform authPlatformType) {
    super();
    this.name = name;
    this.phone = phone;
    this.email = email;
    this.birthday = birthday;
    this.profileImage = profileImage;
    this.authPlatformId = authPlatformId;
    this.authPlatformType = authPlatformType;
  }

  public static UserEntity fromDomain(final Long id, User user) {
    Profile profile = user.getProfile();
    SocialAccount socialAccount = user.getSocialAccount();
    UserEntity userEntity =
        new UserEntity(
            profile.name(),
            profile.phone(),
            profile.email().orElse(null),
            profile.birthday(),
            profile.profileImage().orElse(null),
            socialAccount.authPlatformId(),
            socialAccount.authPlatformType());
    userEntity.setId(id);
    return userEntity;
  }

  public static UserEntity fromDomain(final User user) {
    Profile profile = user.getProfile();
    SocialAccount socialAccount = user.getSocialAccount();
    return new UserEntity(
        profile.name(),
        profile.phone(),
        profile.email().orElse(null),
        profile.birthday(),
        profile.profileImage().orElse(null),
        socialAccount.authPlatformId(),
        socialAccount.authPlatformType());
  }

  public User toDomain() {
    SocialAccount socialAccount = SocialAccount.of(authPlatformId, authPlatformType);
    Profile profile = Profile.of(name, email, phone, birthday);
    return User.createNewUser(socialAccount, profile);
  }
}
