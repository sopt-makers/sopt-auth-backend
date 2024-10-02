package sopt.makers.authentication.user;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProfileTest {

	@Test
	@DisplayName("자신의 정보는 수정이 가능하다")
	public void 자신의_정보는_수정_가능하다() {
		// given
		Profile profile =
				new Profile("강현욱", "rkdgusdn@naver.com", "010-1111-1111", LocalDate.of(2000, 12, 28));

		// when
		Profile updatedName = profile.updateName("현욱갱");
		Profile updatedEmail = profile.updateEmail("rkdsdug@gmail.com");
		Profile updatedPhone = profile.updatePhone("010-2222-2222");
		Profile updatedBirthday = profile.updateBirthday(LocalDate.of(2012, 12, 29));

		// then
		assertThat(updatedName).isNotEqualTo(profile);
		assertThat(updatedEmail).isNotEqualTo(profile);
		assertThat(updatedPhone).isNotEqualTo(profile);
		assertThat(updatedBirthday).isNotEqualTo(profile);

		assertThat(updatedName)
				.isEqualTo(
						new Profile("현욱갱", "rkdgusdn@naver.com", "010-1111-1111", LocalDate.of(2000, 12, 28)));
		assertThat(updatedEmail)
				.isEqualTo(
						new Profile("강현욱", "rkdsdug@gmail.com", "010-1111-1111", LocalDate.of(2000, 12, 28)));
		assertThat(updatedPhone)
				.isEqualTo(
						new Profile("강현욱", "rkdgusdn@naver.com", "010-2222-2222", LocalDate.of(2000, 12, 28)));
		assertThat(updatedBirthday)
				.isEqualTo(
						new Profile("강현욱", "rkdgusdn@naver.com", "010-1111-1111", LocalDate.of(2012, 12, 29)));
	}
}
