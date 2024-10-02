package sopt.makers.authentication.user;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ActivityTest {

	@Test
	@DisplayName("활동 이력은 Null이 될 수 없다")
	public void 성공_활동_NOTNULL() {
		// given
		ActivityList activityList = new ActivityList();
		Activity activity = null;

		// when & then
		assertThatThrownBy(
						() -> {
							activityList.addActivity(activity);
						})
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("같은 활동 이력은 동시에 존재할 수 없다")
	public void 예외_활동_이력_중복() {
		// given
		ActivityList activityList = new ActivityList();
		Activity activity1 = ActivityTestUtil.createActivity();

		activityList.addActivity(activity1);

		// when
		Part part = Part.ANDROID;
		Activity activity2 = ActivityTestUtil.createActivity();

		// then
		assertThatThrownBy(() -> activityList.addActivity(activity2))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("활동 이력은 TEAM ENUM을 가진다")
	public void 성공_TEAM_ENUM_멤버_확인() {
		// given
		Team team = Team.MAKERS;
		Activity activity = new Activity(34, team, Part.IOS, Role.MEMBER);

		// when
		Team team1 = activity.team;

		// then
		assertThat(team1).isEqualTo(Team.MAKERS);
	}

	@Test
	@DisplayName("Team은_NULL이_가능하다")
	public void 성공_TEAM_NULL_가능() {
		// given
		Team team = null;
		Role role = Role.MEMBER;

		Activity activity = new Activity(34, team, Part.IOS, role);
		ActivityList activityList = new ActivityList();

		// when
		Team team1 = activity.team;
		activityList.addActivity(activity);

		// then
		assertThat(team1).isEqualTo(null);
		assertThat(activityList.getFirstActivity().team).isEqualTo(null);
	}

	@Test
	@DisplayName("활동 이력은 활동 파트를 가진다")
	public void 성공_활동_PART_멤버_확인() {
		// given
		Part part = Part.ANDROID;
		Activity activity = new Activity(34, Team.MAKERS, Part.ANDROID, Role.MEMBER);

		// when
		Part part1 = activity.part;

		// then
		assertThat(part1).isEqualTo(Part.ANDROID);
	}

	@Test
	@DisplayName("활동 이력은 NOT NULL이다")
	public void 예외_활동_이력은_NOT_NULL() {
		// given
		ActivityList activityList = new ActivityList();
		Part part = Part.ANDROID;
		Activity activity = new Activity(34, Team.MAKERS, null, Role.MEMBER);

		// when & then
		assertThatThrownBy(
						() -> {
							activityList.addActivity(activity);
						})
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	@DisplayName("활동은 역할을 가진다")
	public void 성공_활동_ROLE_보유() {
		// given
		Role role = Role.MEMBER;
		Activity activity = new Activity(34, Team.MAKERS, Part.ANDROID, role);

		// when
		Role role1 = activity.role;

		// then
		assertThat(role1).isEqualTo(Role.MEMBER);
	}

	@Test
	@DisplayName("역할은 NOT NULL이다")
	public void 역할은_NOT_NULL() {
		// given
		Activity activity = new Activity(34, Team.MAKERS, Part.ANDROID, null);
		ActivityList activityList = new ActivityList();

		// when & then
		assertThatThrownBy(
						() -> {
							activityList.addActivity(activity);
						})
				.isInstanceOf(IllegalArgumentException.class);
	}
}
