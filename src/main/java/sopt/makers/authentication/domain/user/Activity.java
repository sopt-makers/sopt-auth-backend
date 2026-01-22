package sopt.makers.authentication.domain.user;

import static sopt.makers.authentication.domain.user.exception.UserFailure.ROLE_REQUIRES_PART;

import sopt.makers.authentication.domain.user.exception.UserException;

import java.util.Optional;

import lombok.Getter;

@Getter
public class Activity {
  private Long id;
  private final int generation;
  private final Team team;
  private final Part part;
  private final Role role;
  private final boolean isSopt;

  private Activity(Long id, int generation, Team team, Part part, Role role, boolean isSopt) {
    this.id = id;
    this.generation = generation;
    this.team = team;
    this.part = part;
    this.role = role;
    this.isSopt = isSopt;
  }

  public static Activity of(
      int generation, final Team team, final Part part, final boolean isSopt) {
    return new Activity(null, generation, team, part, Role.MEMBER, isSopt);
  }

  public static Activity of(
      Long id,
      int generation,
      final Team team,
      final Part part,
      final Role role,
      final boolean isSopt) {
    return new Activity(id, generation, team, part, role, isSopt);
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
