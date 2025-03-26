package sopt.makers.authentication.domain.user;

import sopt.makers.authentication.domain.auth.SocialAccount;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
public class User {
  private final Long id;
  private final Profile profile;
  private final SocialAccount socialAccount;
  private ActivityList activities;

  public static User createNewUser(final SocialAccount socialAccount, final Profile profile) {
    return User.builder()
        .socialAccount(socialAccount)
        .profile(profile)
        .activities(new ActivityList())
        .build();
  }

  public static User createUser(
      final Long id, final SocialAccount socialAccount, final Profile profile) {
    return User.builder()
        .id(id)
        .socialAccount(socialAccount)
        .profile(profile)
        .activities(new ActivityList())
        .build();
  }

  public void updateSocialAccount(final SocialAccount socialAccount) {
    User.builder()
        .id(this.id)
        .socialAccount(socialAccount)
        .profile(this.profile)
        .activities(this.activities)
        .build();
  }

  public void joinActivity(final Activity activity) {
    this.activities = activities.addActivity(activity);
  }
}
