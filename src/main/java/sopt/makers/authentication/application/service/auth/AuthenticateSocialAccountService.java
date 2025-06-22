package sopt.makers.authentication.application.service.auth;

import sopt.makers.authentication.adapter.out.jwt.service.JwtAuthAccessTokenService;
import sopt.makers.authentication.adapter.out.jwt.service.JwtAuthRefreshTokenService;
import sopt.makers.authentication.application.port.in.auth.AuthenticateSocialAccountUsecase;
import sopt.makers.authentication.application.port.out.auth.OAuthAuthenticator;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Role;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;

import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticateSocialAccountService implements AuthenticateSocialAccountUsecase {
  private final OAuthAuthenticator oAuthAuthenticator;
  private final UserRepository userRepository;
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
    CustomAuthentication customAuthentication =
        new CustomAuthentication(
            user.getId(), null, List.of(new SimpleGrantedAuthority(role.name())));
    String accessToken = jwtAuthAccessTokenProvider.generateJwt(customAuthentication);
    String refreshToken = jwtAuthRefreshTokenProvider.generateJwt(accessToken);

    return AuthenticateTokenInfo.of(accessToken, refreshToken);
  }

  @Override
  public AuthenticateTokenInfo refresh(AuthenticateTokenInfo command) {
    String refreshToken = command.refreshToken();

    jwtAuthRefreshTokenProvider.parse(refreshToken);
    CustomAuthentication customAuthentication =
        jwtAuthAccessTokenProvider.parse(command.accessToken());

    String renewedAccessToken = jwtAuthAccessTokenProvider.generateJwt(customAuthentication);
    String renewedRefreshToken = jwtAuthRefreshTokenProvider.generateJwt(renewedAccessToken);
    return AuthenticateTokenInfo.of(renewedAccessToken, renewedRefreshToken);
  }
}
