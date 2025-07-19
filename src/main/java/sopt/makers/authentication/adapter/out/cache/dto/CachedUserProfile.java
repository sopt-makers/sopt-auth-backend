package sopt.makers.authentication.adapter.out.cache.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

public record CachedUserProfile(
    Long userId,
    String name,
    String profileImage,
    LocalDate birthday,
    String phone,
    String email,
    int lastGeneration,
    List<CachedUserActivity> activities)
    implements Serializable {
  private static final long serialVersionUID = 1L;
}
