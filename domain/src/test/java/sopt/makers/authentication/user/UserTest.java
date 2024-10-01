package sopt.makers.authentication.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	public void 유저는_고유_식별_번호를_가진다() {
		// given
		SocialAccount socialAccount = new SocialAccount("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.IOS, Role.MEMBER);
		Long userId = 1L;
		User user = new User(userId, socialAccount, profile, activity);

		// when
		Long id = user.getId();

		// then
		assertThat(id).isEqualTo(1L);
	}

	@Test
	public void 유저는_소셜_계정_정보를_가진다() {
		// given
		SocialAccount socialAccount = new SocialAccount("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		User user = new User(1L, socialAccount, profile, activity);

		// when
		socialAccount = user.getSocialAccount();

		// then
		assertThat(user.getSocialAccount()).isEqualTo(socialAccount);
	}

	@Test
	public void 유저는_프로필_정보를_가진다() {
		// given
		SocialAccount socialAccount = new SocialAccount("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이미지", "상태메시지", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		User user = new User(1L, socialAccount, profile, activity);

		// when
		Profile profile1 = new Profile("이름", "이미지", "상태메시지", LocalDate.now());

		// then
		assertThat(user.getProfile()).isEqualTo(profile);
	}

	@Test
	public void 유저는_활동_정보를_가진다() {
		// given
		SocialAccount socialAccount = new SocialAccount("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		User user = new User(1L, socialAccount, profile, activity);

		// when
		Activity activity1 = new Activity(34, null, Part.ANDROID, Role.MEMBER);

		// then
		assertThat(user.getActivityHistory().getLastActivity()).isEqualTo(activity1);
	}

	@Test
	public void 유저는_여러_활동_이력을_가질_수_있다() {
		// given
		SocialAccount socialAccount = new SocialAccount("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		Activity activity1 = new Activity(34, null, Part.IOS, Role.MEMBER);
		List activities = List.of(activity, activity1);
		User user = new User(1L, socialAccount, profile, activity);

		// when
		user.join(activity1);
		System.out.println(user.getActivityHistory().getTotalActivitySize());

		// then
		assertThat(user.getActivityHistory().getTotalActivitySize()).isEqualTo(activities.size());
	}

	@Test
	public void 유저의_활동_내역은_중복되어선_안된다() {
		// given
		SocialAccount socialAccount = new SocialAccount("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		Activity activity1 = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		List activities = List.of(activity, activity1);
		User user = new User(1L, socialAccount, profile, activity);

		// when & then
		assertThatThrownBy(() -> user.join(activity1)).isInstanceOf(IllegalArgumentException.class);
	}
}
