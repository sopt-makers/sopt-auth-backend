package sopt.makers.authentication.adapter.out.cache.mapper;

import sopt.makers.authentication.adapter.out.cache.dto.CachedUserActivity;
import sopt.makers.authentication.adapter.out.cache.dto.CachedUserProfile;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Profile;
import sopt.makers.authentication.domain.user.Role;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

  public CachedUserProfile toCache(User user) {
    Profile profile = user.getProfile();
    return new CachedUserProfile(
        user.getId(),
        profile.name(),
        profile.profileImage().orElse(null),
        profile.birthday(),
        profile.phone(),
        profile.email().orElse(null),
        user.getActivities().getLastActivity().getGeneration(),
        user.getActivities().getActivities().stream().map(this::toCachedUserActivity).toList());
  }

  public CachedUserActivity toCachedUserActivity(Activity activity) {
    return new CachedUserActivity(
        activity.getId(),
        activity.getGeneration(),
        activity.getPart().getName(),
        activity.getTeam() != null ? activity.getTeam().getName() : null,
        activity.getRole().name());
  }

  public User toDomain(CachedUserProfile cached) {
    Profile profile =
        Profile.of(
            cached.name(),
            cached.email(),
            cached.phone(),
            cached.birthday(),
            cached.profileImage());

    List<Activity> activities = cached.activities().stream().map(this::toActivity).toList();
    ActivityList activityList = ActivityList.of(activities);

    return User.createUser(cached.userId(), null, profile, activityList);
  }

  public Activity toActivity(CachedUserActivity cached) {
    Part part = Part.findPart(cached.part());
    Team team = cached.team() != null ? Team.findTeam(cached.team()) : null;
    Role role = Role.valueOf(cached.role());
    return Activity.of(cached.activityId(), cached.generation(), team, part, role);
  }
}
