package sopt.makers.authentication.domain.user;

import static sopt.makers.authentication.support.code.domain.failure.UserFailure.NOT_FOUND_TEAM;

import sopt.makers.authentication.support.exception.domain.UserException;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Team {
  MAKERS("메이커스"),
  MEDIA("미디어팀"),
  OPERATION("운영팀");

  private final String name;

  public static Team findTeam(final String team) {
    if (team != null) {
      return Arrays.stream(Team.values())
          .filter(p -> p.name.equals(team))
          .findFirst()
          .orElseThrow(() -> new UserException(NOT_FOUND_TEAM));
    }
    return null;
  }
}
