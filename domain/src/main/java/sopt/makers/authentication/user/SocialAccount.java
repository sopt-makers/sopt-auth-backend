package sopt.makers.authentication.user;

public class SocialAccount {

	private final String authPlatformId;
	private final AuthPlatform authPlatformType;

	public SocialAccount(String authPlatformId, String authPlatformType) {
		validatePlatform(authPlatformType);
		this.authPlatformId = authPlatformId;
		this.authPlatformType = AuthPlatform.find(authPlatformType);
	}

	public String getPlatformId() {
		return authPlatformId;
	}

	public AuthPlatform getPlatformType() {
		return authPlatformType;
	}

	private void validatePlatform(String authPlatform) {
		if (authPlatform == null || authPlatform.isEmpty()) {
			throw new IllegalArgumentException("빈 플랫폼 정보 입니다.");
		}
	}
}
