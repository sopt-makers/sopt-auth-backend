package sopt.makers.authentication.adapter.out.cache.dto;

import java.io.Serializable;

public record CachedUserActivity(long activityId, int generation, String part, String team)
    implements Serializable {
  private static final long serialVersionUID = 1L;
}
