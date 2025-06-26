package sopt.makers.authentication.adapter.out.persistence.entity;

import static lombok.AccessLevel.PROTECTED;

import sopt.makers.authentication.domain.auth.AuthPlatform;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.User;

import java.time.LocalDate;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;

import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "users",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_AUTH_PLATFORM_ID_AND_AUTH_PLATFORM_TYPE",
          columnNames = {"auth_platform_id", "auth_platform_type"})
    })
public class UserEntity extends BaseEntity {

  @NotNull private String name;
  @NotNull private String phone;
  private String email;
  private LocalDate birthday;
  @NotNull private String authPlatformId;
  private String profileImage;

  @NotNull
  @Enumerated(EnumType.STRING)
  private AuthPlatform authPlatformType;

  @OneToMany(mappedBy = "user")
  private List<UserActivityHistoryEntity> userActivityHistoryList;

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

  public static UserEntity fromDomain(final User user) {
    Profile profile = user.getProfile();
    SocialAccount socialAccount = user.getSocialAccount();
    boolean hasId = user.getId() != null;
    UserEntity userEntity =
        new UserEntity(
            profile.name(),
            profile.phone(),
            profile.email().orElse(null),
            profile.birthday(),
            profile.profileImage().orElse(null),
            socialAccount.authPlatformId(),
            socialAccount.authPlatformType());
    if (hasId) {
      userEntity.setId(user.getId());
    }
    return userEntity;
  }

  public User toDomain() {
    SocialAccount socialAccount = SocialAccount.of(authPlatformId, authPlatformType);
    Profile profile = Profile.of(name, email, phone, birthday);
    if (userActivityHistoryList == null || userActivityHistoryList.isEmpty()) {
      return User.createUser(super.getId(), socialAccount, profile);
    }
    List<Activity> activityList =
        userActivityHistoryList.stream().map(UserActivityHistoryEntity::toDomain).toList();
    return User.createUser(super.getId(), socialAccount, profile, ActivityList.of(activityList));
  }
}
