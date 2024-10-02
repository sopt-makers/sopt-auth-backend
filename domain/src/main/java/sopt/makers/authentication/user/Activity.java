package sopt.makers.authentication.user;

public record Activity(Integer generation, Team team, Part part, Role role) {

	public Activity(Integer generation, Team team, Part part) {
		this(generation, team, part, Role.MEMBER);
	}

	public void validateActivityContentsEmpty() {
		if (this.generation == null || this.role == null) {
			throw new IllegalArgumentException("활동 정보가 비어있습니다.");
		}

		if (this.role.isPartRequired() && this.part == null) {
			throw new IllegalArgumentException("해당 Role은 part 필드가 필수입니다.");
		}
	}
}
