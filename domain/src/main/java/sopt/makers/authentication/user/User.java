package sopt.makers.authentication.user;

public class User {

	private final Long id;

	private final SocialAccount socialAccount;

	private final Profile profile;

	private final ActivityList activities;

	public User(Long id, SocialAccount socialAccount, Profile profile, Activity activity) {
		this.id = id;
		this.socialAccount = socialAccount;
		this.profile = profile;
		this.activities = new ActivityList();
		this.activities.addActivity(activity);
	}

	public Profile getProfile() {
		return profile;
	}

	public Long getId() {
		return id;
	}

	public SocialAccount getSocialAccount() {
		return socialAccount;
	}

	public ActivityList getActivityHistory() {
		return activities;
	}

	public void join(Activity activity) {
		this.activities.addActivity(activity);
	}
}
