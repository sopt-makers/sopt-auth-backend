package sopt.makers.authentication.domain.user;

import static sopt.makers.authentication.support.code.domain.failure.UserFailure.ROLE_REQUIRES_PART;

import sopt.makers.authentication.support.exception.domain.UserException;

import java.util.Optional;

import lombok.Getter;

@Getter
public class Activity {
  private Long id;
  private Long userId;
  private final int generation;
  private final Team team;
  private final Part part;
  private final Role role;

  private Activity(Long id, Long userId, int generation, Team team, Part part, Role role) {
    this.id = id;
    this.generation = generation;
    this.userId = userId;
    this.team = team;
    this.part = part;
    this.role = role;
  }

  public static Activity of(int generation, final Team team, final Part part) {
    return new Activity(null, null, generation, team, part, Role.MEMBER);
  }

  public static Activity of(int generation, final Team team, final Part part, final Role role) {
    return new Activity(null, null, generation, team, part, role);
  }

  public static Activity of(
      Long id, int generation, final Team team, final Part part, final Role role) {
    return new Activity(id, null, generation, team, part, role);
  }

  public static Activity of(Long id, Long userId, int generation, Team team, Part part, Role role) {
    return new Activity(id, userId, generation, team, part, role);
  }

  public Optional<Team> optionalTeam() {
    return Optional.ofNullable(team);
  }

  public void validateActivityContentsEmpty() {
    boolean isActivityContentsEmpty = this.role.isPartRequired() && this.part == null;

    if (isActivityContentsEmpty) {
      throw new UserException(ROLE_REQUIRES_PART);
    }
  }
}
