package sopt.makers.authentication.user;

public class User {

	private final long id;

	private final SocialAccount socialAccount;

	private final Profile profile;

	private ActivityList activities;

	public User(long id, SocialAccount socialAccount, Profile profile) {
		this.id = id;
		this.socialAccount = socialAccount;
		this.profile = profile;
		this.activities = new ActivityList();
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

	public ActivityList getActivityHistory() {
		return activities;
	}

	public void join(Activity activity) {
		this.activities = activities.addActivity(activity);
	}
}
