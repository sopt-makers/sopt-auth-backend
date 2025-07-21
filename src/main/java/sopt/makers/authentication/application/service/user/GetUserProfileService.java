package sopt.makers.authentication.application.service.user;

import sopt.makers.authentication.application.port.in.user.GetUserProfileUsecase;
import sopt.makers.authentication.application.port.out.user.UserCacheRepository;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.User;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserProfileService implements GetUserProfileUsecase {

  private final UserRepository userRepository;
  private final UserCacheRepository userCacheRepository;

  @Override
  public List<UserProfileAndActivityInfo> getUserInformation(List<Long> userIds) {
    Map<Long, User> cache = userCacheRepository.getAllPresent(userIds);

    loadMissingUsers(userIds, cache);
    return userIds.stream()
        .map(cache::get)
        .map(user -> UserProfileAndActivityInfo.of(user, user.getActivities()))
        .toList();
  }

  private void loadMissingUsers(List<Long> userIds, Map<Long, User> cache) {
    List<Long> missingIds = userIds.stream().filter(id -> !cache.containsKey(id)).toList();

    if (missingIds.isEmpty()) {
      return;
    }
    userRepository
        .findAllById(missingIds)
        .forEach(
            user -> {
              userCacheRepository.put(user.getId(), user);
              cache.put(user.getId(), user);
            });
  }

  @Override
  public List<UserProfileAndActivityInfo> getUserInformationByActivity(
      Integer generation, Part part) {
    List<User> userList = userRepository.findAllByGenerationAndPart(generation, part);
    return userList.stream()
        .map(user -> UserProfileAndActivityInfo.of(user, user.getActivities()))
        .toList();
  }

  @Override
  public UserCountByGeneration getUserCountByGeneration(int generation) {
    int count = userRepository.countByGeneration(generation);
    return new UserCountByGeneration(count);
  }
}
