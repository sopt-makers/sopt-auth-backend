package sopt.makers.authentication.user;

public interface UserPort {

	User findUserById(Long id);

	User saveUser(User user);

	void deleteUserById(Long id);

	Profile findProfileByUser(User user);

	SocialAccount findSocialAccountByUser(User user);

	ActivityList findActivitiesByUser(User user);

	// 사용자가 프로필에 등록한 이메일은 유지할지, 소셜 계정에 따라 프로필 이메일도 변경할지
	SocialAccount exchangeSocialPlatform(User user, SocialAccount socialAccount, Profile profile);
}
