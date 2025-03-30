package sopt.makers.authentication.database.rdb.repository.user.activity;

import sopt.makers.authentication.database.rdb.entity.UserActivityHistoryEntity;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserActivityHistoryRegister {

  private final UserActivityHistoryJpaRepository userActivityHistoryJpaRepository;

  public void save(UserActivityHistoryEntity userActivityHistoryEntity) {
    userActivityHistoryJpaRepository.save(userActivityHistoryEntity);
  }
}
