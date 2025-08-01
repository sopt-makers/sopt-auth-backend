package sopt.makers.authentication.adapter.out.cache.dto;

import java.io.Serial;
import java.io.Serializable;

public record CachedUserActivity(
    long activityId, int generation, String part, String team, String role)
    implements Serializable {
  @Serial private static final long serialVersionUID = 1L;
}
