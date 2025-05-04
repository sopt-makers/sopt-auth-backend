package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Role;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.jwt.service.JwtAuthAccessTokenService;
import sopt.makers.authentication.support.jwt.service.JwtAuthRefreshTokenService;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;
import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;
import sopt.makers.authentication.usecase.user.port.out.UserActivityHistoryRepository;
import sopt.makers.authentication.usecase.user.port.out.UserRepository;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticateSocialAccountService implements AuthenticateSocialAccountUsecase {
  private final OAuthAuthenticator oAuthAuthenticator;
  private final UserRepository userRepository;
  private final UserActivityHistoryRepository userActivityHistoryRepository;
  private final JwtAuthAccessTokenService jwtAuthAccessTokenProvider;
  private final JwtAuthRefreshTokenService jwtAuthRefreshTokenProvider;

  @Override
  public AuthenticateTokenInfo authenticate(AuthenticateSocialAccountCommand command) {
    String authPlatformId =
        oAuthAuthenticator.getIdentifier(command.token(), command.authPlatform());
    User user =
        userRepository.findBySocialAccount(
            SocialAccount.of(authPlatformId, command.authPlatform()));
    ActivityList activityList = user.getActivities();
    Role role = activityList.getLastActivity().getRole();
    CustomAuthentication customAuthentication = new CustomAuthentication(user.getId(), role);
    String accessToken = jwtAuthAccessTokenProvider.generate(customAuthentication);
    String refreshToken = jwtAuthRefreshTokenProvider.generate(accessToken);

    return AuthenticateTokenInfo.of(accessToken, refreshToken);
  }

  @Override
  public AuthenticateTokenInfo refresh(AuthenticateTokenInfo command) {
    String refreshToken = command.refreshToken();

    jwtAuthRefreshTokenProvider.parse(refreshToken);
    CustomAuthentication customAuthentication =
        jwtAuthAccessTokenProvider.parse(command.accessToken());

    String renewedAccessToken = jwtAuthAccessTokenProvider.generate(customAuthentication);
    String renewedRefreshToken = jwtAuthRefreshTokenProvider.generate(renewedAccessToken);
    return AuthenticateTokenInfo.of(renewedAccessToken, renewedRefreshToken);
  }
}
