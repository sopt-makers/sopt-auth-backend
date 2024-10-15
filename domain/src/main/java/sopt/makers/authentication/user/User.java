package sopt.makers.authentication.user;

import lombok.Builder;

public class User {

	private final long id;
	private final Profile profile;
	private SocialAccount socialAccount;
	private ActivityList activities;

	@Builder
	public User(
			long id,
			final SocialAccount socialAccount,
			final Profile profile,
			final ActivityList activities) {
		this.id = id;
		this.socialAccount = socialAccount;
		this.profile = profile;
		this.activities = activities;
	}

	public static User createNewUser(
			long id, final SocialAccount socialAccount, final Profile profile) {
		return User.builder()
				.id(id)
				.socialAccount(socialAccount)
				.profile(profile)
				.activities(new ActivityList())
				.build();
	}

	public Profile getProfile() {
		return profile;
	}

	public long getId() {
		return id;
	}

	public SocialAccount getSocialAccount() {
		return socialAccount;
	}

	public User updateSocialAccount(final SocialAccount socialAccount) {
		return new User(this.id, socialAccount, this.profile, this.activities);
	}

	public ActivityList getActivityHistory() {
		return activities;
	}

	public void join(final Activity activity) {
		this.activities = activities.addActivity(activity);
	}
}
