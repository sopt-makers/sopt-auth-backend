package sopt.makers.authentication.user;

import java.util.Arrays;

public enum AuthPlatform {
	GOOGLE,
	APPLE;

	public static AuthPlatform find(final String platform) {

		return Arrays.stream(AuthPlatform.values())
				.filter(p -> p.name().equals(platform))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("지원하지 않는 소셜 플랫폼입니다 : " + platform));
	}
}
