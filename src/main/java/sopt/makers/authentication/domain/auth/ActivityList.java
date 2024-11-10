package sopt.makers.authentication.domain.auth;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.*;

import lombok.*;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ActivityList {
  private final List<Activity> activities;

  public ActivityList() {
    this.activities = new ArrayList<>();
  }

  public ActivityList addActivity(@NotNull final Activity activity) {
    validateActivityDuplication(activity);
    List<Activity> updatedActivities = new ArrayList<>(this.activities);
    updatedActivities.add(activity);
    return new ActivityList(updatedActivities);
  }

  public Activity getFirstActivity() {
    return activities.getFirst();
  }

  public Activity getLastActivity() {
    return activities.getLast();
  }

  public int getTotalActivitySize() {
    return activities.size();
  }

  private void validateActivityDuplication(final Activity activity) {
    activities.stream()
        .filter(a -> a.equals(activity))
        .findAny()
        .ifPresent(
            a -> {
              throw new IllegalArgumentException("이미 존재하는 활동 정보입니다.");
            });
  }
}
