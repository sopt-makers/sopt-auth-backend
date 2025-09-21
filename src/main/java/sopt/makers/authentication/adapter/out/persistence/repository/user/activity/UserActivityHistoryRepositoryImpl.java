package sopt.makers.authentication.adapter.out.persistence.repository.user.activity;

import sopt.makers.authentication.adapter.out.persistence.entity.UserActivityHistoryEntity;
import sopt.makers.authentication.application.port.out.user.UserActivityHistoryRepository;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.User;

import java.util.List;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserActivityHistoryRepositoryImpl implements UserActivityHistoryRepository {
  private final UserActivityHistoryRegister userActivityHistoryRegister;

  @Transactional
  @Override
  public void save(User user, Activity activity) {
    UserActivityHistoryEntity userActivityHistoryEntity =
        UserActivityHistoryEntity.fromDomain(user, activity);
    userActivityHistoryRegister.save(userActivityHistoryEntity);
  }

  @Override
  public void update(User user, ActivityList activityList) {
    List<UserActivityHistoryEntity> entities =
        activityList.getActivities().stream()
            .map(activity -> UserActivityHistoryEntity.fromDomain(user, activity))
            .toList();
    userActivityHistoryRegister.saveAll(entities);
  }

  @Transactional
  @Override
  public void deleteByUserId(Long userId) {
    userActivityHistoryRegister.deleteByUserId(userId);
  }
}
