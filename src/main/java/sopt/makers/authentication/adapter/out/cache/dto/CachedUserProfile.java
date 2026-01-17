package sopt.makers.authentication.adapter.out.cache.dto;

import java.util.List;

public record CachedUserProfile(
    Long userId,
    String name,
    String profileImage,
    String birthday,
    String phone,
    String email,
    int lastGeneration,
    List<CachedUserActivity> activities) {}
