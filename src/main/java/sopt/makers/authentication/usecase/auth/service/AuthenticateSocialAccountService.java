package sopt.makers.authentication.usecase.auth.service;

import sopt.makers.authentication.domain.auth.SocialAccount;
import sopt.makers.authentication.domain.user.Role;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.support.jwt.provider.JwtAuthAccessTokenProvider;
import sopt.makers.authentication.support.jwt.provider.JwtAuthRefreshTokenProvider;
import sopt.makers.authentication.support.security.authentication.CustomAuthentication;
import sopt.makers.authentication.usecase.auth.port.in.AuthenticateSocialAccountUsecase;
import sopt.makers.authentication.usecase.auth.port.out.OAuthAuthenticator;
import sopt.makers.authentication.usecase.auth.port.out.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthenticateSocialAccountService implements AuthenticateSocialAccountUsecase {
  private final OAuthAuthenticator oAuthAuthenticator;
  private final UserRepository userRepository;
  private final JwtAuthAccessTokenProvider jwtAuthAccessTokenProvider;
  private final JwtAuthRefreshTokenProvider jwtAuthRefreshTokenProvider;

  @Override
  public AuthenticateTokenInfo authenticate(AuthenticateSocialAccountCommand command) {
    String authPlatformId =
        oAuthAuthenticator.getIdentifier(command.token(), command.authPlatform());
    User user =
        userRepository.findBySocialAccount(
            SocialAccount.of(authPlatformId, command.authPlatform()));
    List<Role> roles = List.of(user.getActivities().getLastActivity().getRole());
    CustomAuthentication customAuthentication = new CustomAuthentication(user.getId(), roles);
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
