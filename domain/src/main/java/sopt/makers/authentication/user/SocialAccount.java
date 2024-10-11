package sopt.makers.authentication.user;

public record SocialAccount(String authPlatformId, AuthPlatform authPlatformType) {

	public static SocialAccount of(final String authPlatformId, final String authPlatformType) {
		return new SocialAccount(authPlatformId, AuthPlatform.find(authPlatformType));
	}

	public SocialAccount {
		validatePlatform(authPlatformId);
	}

	// 플랫폼 정보가 비어 있는지 검증
	private void validatePlatform(final String authPlatformId) {
		if (authPlatformId == null || authPlatformId.isEmpty()) {
			throw new IllegalArgumentException("빈 플랫폼 정보입니다.");
		}
	}
}
