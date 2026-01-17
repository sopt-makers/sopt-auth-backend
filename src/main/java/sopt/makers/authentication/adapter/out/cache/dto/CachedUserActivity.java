package sopt.makers.authentication.adapter.out.cache.dto;

public record CachedUserActivity(
    long activityId, int generation, String part, String team, String role) {}
