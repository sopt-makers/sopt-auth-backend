package sopt.makers.authentication.domain.user;

import static sopt.makers.authentication.domain.user.exception.UserFailure.NOT_FOUND_ROLE;

import sopt.makers.authentication.domain.user.exception.UserException;

import java.util.Arrays;

public enum Role {
  MEMBER,
  PRESIDENT,
  VICE_PRESIDENT,
  TEAM_LEADER,
  PART_LEADER,
  GENERAL_AFFAIRS,
  ART_DIRECTOR;

  public static Role findRole(final String role) {
    return Arrays.stream(Role.values())
        .filter(r -> r.name().equals(role))
        .findFirst()
        .orElseThrow(() -> new UserException(NOT_FOUND_ROLE));
  }

  public boolean isPartRequired() {
    return !(this == PRESIDENT || this == VICE_PRESIDENT);
  }
}
