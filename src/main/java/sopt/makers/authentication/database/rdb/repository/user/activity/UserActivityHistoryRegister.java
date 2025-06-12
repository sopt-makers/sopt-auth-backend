package sopt.makers.authentication.database.rdb.repository.user.activity;

import sopt.makers.authentication.database.rdb.entity.UserActivityHistoryEntity;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserActivityHistoryRegister {

  private final UserActivityHistoryJpaRepository userActivityHistoryJpaRepository;

  public void save(UserActivityHistoryEntity userActivityHistoryEntity) {
    userActivityHistoryJpaRepository.save(userActivityHistoryEntity);
  }

  public void saveAll(List<UserActivityHistoryEntity> userActivityHistoryEntities) {
    userActivityHistoryJpaRepository.saveAll(userActivityHistoryEntities);
  }
}
