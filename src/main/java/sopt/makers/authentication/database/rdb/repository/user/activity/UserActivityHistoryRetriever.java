package sopt.makers.authentication.database.rdb.repository.user.activity;

import sopt.makers.authentication.database.rdb.entity.UserActivityHistoryEntity;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserActivityHistoryRetriever {

  private final UserActivityHistoryJpaRepository userActivityHistoryJpaRepository;

  public ActivityList findByUser(Long userId) {
    List<UserActivityHistoryEntity> userActivityHistoryEntity =
        userActivityHistoryJpaRepository.findByUserId(userId);

    List<Activity> activities =
        userActivityHistoryEntity.stream().map(UserActivityHistoryEntity::toDomain).toList();

    return ActivityList.of(activities);
  }
}
