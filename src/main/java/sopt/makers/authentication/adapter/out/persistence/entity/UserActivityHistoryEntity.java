package sopt.makers.authentication.adapter.out.persistence.entity;

import static lombok.AccessLevel.PROTECTED;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Role;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "user_activity_histories",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_USER_ID_AND_GENERATION",
          columnNames = {"user_id", "generation", "is_sopt"})
    })
public class UserActivityHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Setter(value = PROTECTED)
  private Long id;

  @NotNull
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private UserEntity user;

  @Min(1)
  private int generation;

  @Enumerated(EnumType.STRING)
  private Team team;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Part part;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Role role;

  @NotNull
  @Column(name = "is_sopt")
  private boolean isSopt;

  private UserActivityHistoryEntity(
      final UserEntity user,
      final int generation,
      final Team team,
      final Part part,
      final Role role,
      final boolean isSopt) {
    this.user = user;
    this.generation = generation;
    this.team = team;
    this.part = part;
    this.role = role;
    this.isSopt = isSopt;
  }

  public static UserActivityHistoryEntity fromDomain(final User user, final Activity activity) {
    UserEntity userEntity = UserEntity.fromDomain(user);
    UserActivityHistoryEntity userActivityHistoryEntity =
        new UserActivityHistoryEntity(
            userEntity,
            activity.getGeneration(),
            activity.optionalTeam().orElse(null),
            activity.getPart(),
            activity.getRole(),
            activity.isSopt());

    if (activity.getId() != null) {
      userActivityHistoryEntity.setId(activity.getId());
    }
    return userActivityHistoryEntity;
  }

  public Activity toDomain() {
    return Activity.of(id, generation, team, part, role, isSopt);
  }
}
