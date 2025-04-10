package sopt.makers.authentication.database.rdb.repository.user.activity;

import sopt.makers.authentication.database.rdb.entity.UserActivityHistoryEntity;
import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.ActivityList;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.usecase.user.port.out.UserActivityHistoryRepository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Repository
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserActivityHistoryRepositoryImpl implements UserActivityHistoryRepository {

  private final UserActivityHistoryRegister userActivityHistoryRegister;
  private final UserActivityHistoryRetriever userActivityHistoryRetriever;

  @Transactional
  @Override
  public void save(User user, Activity activity) {
    UserActivityHistoryEntity userActivityHistoryEntity =
        UserActivityHistoryEntity.fromDomain(user, activity);
    userActivityHistoryRegister.save(userActivityHistoryEntity);
  }

  public ActivityList findByUser(Long userId) {
    return userActivityHistoryRetriever.findByUser(userId);
  }
}
