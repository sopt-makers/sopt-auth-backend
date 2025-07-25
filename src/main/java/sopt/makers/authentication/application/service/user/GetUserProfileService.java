package sopt.makers.authentication.application.service.user;

import sopt.makers.authentication.application.port.in.user.GetUserProfileUsecase;
import sopt.makers.authentication.application.port.out.user.UserRepository;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;
import sopt.makers.authentication.domain.user.UserOrderBy;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetUserProfileService implements GetUserProfileUsecase {

  private static final String USER_ACTIVITY_SORT_ALIAS = "a.";

  private final UserRepository userRepository;

  @Override
  public List<UserProfileAndActivityInfo> getUserInformation(List<Long> userIds) {
    List<User> userList = userRepository.findAllById(userIds);

    return userList.stream()
        .map(user -> UserProfileAndActivityInfo.of(user, user.getActivities()))
        .toList();
  }

  @Override
  public PaginatedUserProfiles getUserInformationByFilters(
      Integer generation,
      Part part,
      String name,
      Team team,
      int offset,
      int limit,
      UserOrderBy orderBy) {
    Pageable pageable = getPageable(offset, limit, orderBy);

    Page<User> userEntityPage =
        userRepository.findAllByGenerationAndPartAndNameAndTeam(
            generation, part, name, team, pageable);

    long totalCount = userEntityPage.getTotalElements();
    boolean hasNext = userEntityPage.hasNext();

    List<UserProfileAndActivityInfo> profiles =
        userEntityPage.getContent().stream()
            .map(user -> UserProfileAndActivityInfo.of(user, user.getActivities()))
            .toList();

    return new PaginatedUserProfiles(profiles, hasNext, totalCount);
  }

  private static Pageable getPageable(int offset, int limit, UserOrderBy orderBy) {
    if (orderBy.isGenerationOrder()) {
      return PageRequest.of(
          offset / limit,
          limit,
          Sort.by(orderBy.getDirection(), USER_ACTIVITY_SORT_ALIAS + orderBy.getField()));
    } else {
      return PageRequest.of(
          offset / limit, limit, Sort.by(orderBy.getDirection(), orderBy.getField()));
    }
  }

  @Override
  public UserCountByGeneration getUserCountByGeneration(int generation) {
    int count = userRepository.countByGeneration(generation);
    return new UserCountByGeneration(count);
  }
}
