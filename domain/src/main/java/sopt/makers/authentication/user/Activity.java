package sopt.makers.authentication.user;

import java.util.Objects;

public class Activity {

	public final Integer generation;

	public final Team team;

	public final Part part;

	public final Role role;

	public Activity(int generation, Team team, Part part, Role role) {
		this.generation = generation;
		this.team = team;
		this.part = part;
		this.role = role;
	}

	public Activity(int generation, Team team, Part part) {
		this.generation = generation;
		this.team = team;
		this.part = part;
		this.role = Role.MEMBER;
	}

	public int getGeneration() {
		return generation;
	}

	public void validateActivityContentsEmpty(Activity activity) {
		if (activity == null
				|| activity.generation == null
				|| activity.part == null
				|| activity.role == null) {
			throw new IllegalArgumentException("활동 정보가 비어있습니다..");
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Activity activity = (Activity) o;
		return Objects.equals(generation, activity.generation)
				&& Objects.equals(team, activity.team)
				&& Objects.equals(part, activity.part)
				&& Objects.equals(role, activity.role);
	}

	@Override
	public int hashCode() {
		return Objects.hash(generation, team, part, role);
	}
}
