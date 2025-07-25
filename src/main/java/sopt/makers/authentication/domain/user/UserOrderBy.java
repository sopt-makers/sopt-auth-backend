package sopt.makers.authentication.domain.user;

import org.springframework.data.domain.Sort;

import lombok.Getter;

@Getter
public enum UserOrderBy {
  LATEST_REGISTERED("id", Sort.Direction.DESC),
  OLDEST_REGISTERED("id", Sort.Direction.ASC),
  LATEST_GENERATION("generation", Sort.Direction.DESC),
  OLDEST_GENERATION("generation", Sort.Direction.ASC);

  private final String field;
  private final Sort.Direction direction;

  UserOrderBy(String field, Sort.Direction direction) {
    this.field = field;
    this.direction = direction;
  }

  public boolean isGenerationOrder() {
    return this == LATEST_GENERATION || this == OLDEST_GENERATION;
  }
}
