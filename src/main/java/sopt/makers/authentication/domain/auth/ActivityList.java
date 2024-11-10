package sopt.makers.authentication.domain.auth;

import static sopt.makers.authentication.support.common.code.failure.DomainFailure.DUPLICATE_ACTIVITY;

import sopt.makers.authentication.support.common.exception.DomainException;

import java.util.ArrayList;
import java.util.List;

import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

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
              throw new DomainException(DUPLICATE_ACTIVITY);
            });
  }
}
