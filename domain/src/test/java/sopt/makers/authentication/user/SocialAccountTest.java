package sopt.makers.authentication.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SocialAccountTest {

	@Test
	@DisplayName("소셜 계정은 플랫폼 타입을 가진다")
	public void 소셜_계정은_플랫폼_타입을_가진다() {
		// given
		AuthPlatform authPlatform = AuthPlatform.GOOGLE;
		SocialAccount socialAccount = SocialAccount.of("1L", "GOOGLE");

		// when & then
		assertThat(socialAccount.authPlatformType()).isEqualTo(authPlatform);
	}

	@Test
	@DisplayName("플랫폼 생성시 Null 검증이 필요하다")
	public void 플랫폼_생성시_NOT_NULL_검사() {
		// given
		String authPlatform = null;

		// when & then
		assertThatThrownBy(() -> SocialAccount.of("1L", null))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("플랫폼은 APPLE, GOOGLE만 가능하다")
	public void 플랫폼은_KAKAO_GOOGLE() {
		// given
		SocialAccount googleAccount = SocialAccount.of("1L", "GOOGLE");
		SocialAccount appleAccount = SocialAccount.of("1L", "APPLE");

		// when
		AuthPlatform apple = AuthPlatform.APPLE;
		AuthPlatform google = AuthPlatform.GOOGLE;

		// then
		assertThat(googleAccount.authPlatformType()).isEqualTo(google);
		assertThat(appleAccount.authPlatformType()).isEqualTo(apple);
	}

	@Test
	@DisplayName("플랫폼은 GOOGLE, APPLE 이외는 불허한다")
	public void 플랫폼_GOOGLE_APPLE_이외는_불허한다() {

		// given
		// when & then
		assertThatThrownBy(() -> SocialAccount.of("1L", "KAKAO"))
				.isInstanceOf(IllegalArgumentException.class);
	}
}
