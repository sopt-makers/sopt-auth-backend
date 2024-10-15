package sopt.makers.authentication.user;

import java.util.Arrays;

public enum Role {
	MEMBER,
	PRESIDENT,
	VICE_PRESIDENT,
	TEAM_LEADER,
	PART_LEADER;

	public static Role findRole(final String role) {
		return Arrays.stream(Role.values())
				.filter(r -> r.name().equals(role))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 역할입니다 : " + role));
	}

	public boolean isPartRequired() {
		return !(this == PRESIDENT || this == VICE_PRESIDENT);
	}
}
