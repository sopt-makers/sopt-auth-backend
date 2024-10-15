package sopt.makers.authentication.user;

import java.util.Arrays;

public enum Part {
	ANDROID,
	IOS,
	SERVER,
	DESIGN,
	PLAN,
	WEB;

	public static Part findPart(final String part) {

		return Arrays.stream(Part.values())
				.filter(p -> p.name().equals(part))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("존재하지 않는 파트입니다 : " + part));
	}
}
