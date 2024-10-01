package sopt.makers.authentication.user;

public class User {

	private final Long id;

	private final SocialAccount socialAccount;

	private final Profile profile;

	private final ActivityList activityHistory;

	public User(Long id, SocialAccount socialAccount, Profile profile, Activity activity) {
		this.id = id;
		this.socialAccount = socialAccount;
		this.profile = profile;
		this.activityHistory = new ActivityList();
		this.activityHistory.addActivity(activity);
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
		return activityHistory;
	}

	public void join(Activity activity) {
		this.activityHistory.addActivity(activity);
	}
}
