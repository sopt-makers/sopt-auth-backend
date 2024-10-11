package sopt.makers.authentication.user;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class UserTest {

	@Test
	public void 유저는_고유_식별_번호를_가진다() {
		// given
		SocialAccount socialAccount = SocialAccount.of("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.IOS, Role.MEMBER);
		Long userId = 1L;
		User user = new User(userId, socialAccount, profile);

		// when
		Long id = user.getId();

		// then
		assertThat(id).isEqualTo(1L);
	}

	@Test
	@DisplayName("유저는 소셜 계정 정보를 가진다")
	public void 유저는_소셜_계정_정보를_가진다() {
		// given
		SocialAccount socialAccount = SocialAccount.of("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		User user = new User(1L, socialAccount, profile);

		// when
		socialAccount = user.getSocialAccount();

		// then
		assertThat(user.getSocialAccount()).isEqualTo(socialAccount);
	}

	@Test
	@DisplayName("유저는 소셜 계정 정보를 변경할 수 있다")
	public void 유저는_소셜_계정_정보를_변경할_수_있다() {
		// given
		SocialAccount socialAccount = SocialAccount.of("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		User user = new User(1L, socialAccount, profile);
		SocialAccount exchangeSocialAccount = SocialAccount.of("2L", "GOOGLE");

		// when
		user.updateSocialAccount(exchangeSocialAccount);

		// then
		assertThat(user.getSocialAccount()).isEqualTo(exchangeSocialAccount);
	}

	@Test
	@DisplayName("유저는 프로필 정보를 가진다")
	public void 유저는_프로필_정보를_가진다() {
		// given
		SocialAccount socialAccount = SocialAccount.of("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이미지", "상태메시지", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		User user = new User(1L, socialAccount, profile);

		// when
		Profile profile1 = new Profile("이름", "이미지", "상태메시지", LocalDate.now());

		// then
		assertThat(user.getProfile()).isEqualTo(profile);
	}

	@Test
	@DisplayName("유저는 활동 정보를 가진다")
	public void 유저는_활동_정보를_가진다() {
		// given
		SocialAccount socialAccount = SocialAccount.of("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		User user = new User(1L, socialAccount, profile);

		// when
		Activity activity1 = new Activity(35, null, Part.ANDROID, Role.MEMBER);
		user.join(activity1);
		user.join(activity);

		// then
		assertThat(user.getActivityHistory().getLastActivity()).isEqualTo(activity);
	}

	@Test
	@DisplayName("유저는 여러 활동 이력을 가질 수 있다")
	public void 유저는_여러_활동_이력을_가질_수_있다() {
		// given
		SocialAccount socialAccount = SocialAccount.of("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		Activity activity1 = new Activity(34, null, Part.IOS, Role.MEMBER);
		List activities = List.of(activity, activity1);
		User user = new User(1L, socialAccount, profile);

		// when
		user.join(activity1);
		user.join(activity);
		System.out.println(user.getActivityHistory().getTotalActivitySize());

		// then
		assertThat(user.getActivityHistory().getTotalActivitySize()).isEqualTo(activities.size());
	}

	@Test
	@DisplayName("유저의 활동 내역은 중복되어선 안된다")
	public void 유저의_활동_내역은_중복되어선_안된다() {
		// given
		SocialAccount socialAccount = SocialAccount.of("1L", "GOOGLE");
		Profile profile = new Profile("이름", "이메일", "010-1234-5678", LocalDate.now());
		Activity activity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		Activity sameActivity = new Activity(34, null, Part.ANDROID, Role.MEMBER);
		User user = new User(1L, socialAccount, profile);

		// when
		user.join(activity);

		// then
		assertThat(activity.equals(sameActivity)).isTrue();
		assertThatThrownBy(() -> user.join(sameActivity)).isInstanceOf(IllegalArgumentException.class);
	}
}
