package sopt.makers.authentication.adapter.out.persistence.repository.user;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserRemover {
  private final UserJpaRepository userJpaRepository;

  public void deleteById(Long userId) {
    userJpaRepository.deleteById(userId);
  }
}
