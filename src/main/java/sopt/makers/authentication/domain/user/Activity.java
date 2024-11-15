package sopt.makers.authentication.domain.user;

import static sopt.makers.authentication.support.code.domain.failure.UserFailure.ROLE_REQUIRES_PART;

import sopt.makers.authentication.support.exception.domain.UserException;

import java.util.Optional;

import jakarta.validation.constraints.NotNull;

public record Activity(
    int generation, Optional<Team> team, Optional<Part> part, @NotNull Role role) {

  public static Activity of(int generation, final Team team, final Part part) {
    return new Activity(
        generation, Optional.ofNullable(team), Optional.ofNullable(part), Role.MEMBER);
  }

  public static Activity of(int generation, final Team team, final Part part, final Role role) {
    return new Activity(generation, Optional.ofNullable(team), Optional.ofNullable(part), role);
  }

  public void validateActivityContentsEmpty() {
    boolean isActivityContentsEmpty = this.role.isPartRequired() && this.part.isEmpty();

    if (isActivityContentsEmpty) {
      throw new UserException(ROLE_REQUIRES_PART);
    }
  }
}
