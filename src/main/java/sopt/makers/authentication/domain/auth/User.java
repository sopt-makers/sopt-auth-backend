package sopt.makers.authentication.domain.auth;

import lombok.*;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class User {

  private final long id;
  private final Profile profile;
  private final SocialAccount socialAccount;
  private ActivityList activities;

  public static User createNewUser(
      long id, final SocialAccount socialAccount, final Profile profile) {
    return User.builder()
        .id(id)
        .socialAccount(socialAccount)
        .profile(profile)
        .activities(new ActivityList())
        .build();
  }

  public User updateSocialAccount(final SocialAccount socialAccount) {
    return new User(this.id, this.profile, socialAccount, this.activities);
  }

  public ActivityList getActivityHistory() {
    return activities;
  }

  public void joinActivity(final Activity activity) {
    this.activities = activities.addActivity(activity);
  }
}
