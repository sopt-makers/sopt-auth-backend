package sopt.makers.authentication.database.rdb.entity;

import static lombok.AccessLevel.PROTECTED;

import sopt.makers.authentication.domain.user.Activity;
import sopt.makers.authentication.domain.user.Part;
import sopt.makers.authentication.domain.user.Role;
import sopt.makers.authentication.domain.user.Team;
import sopt.makers.authentication.domain.user.User;

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

@Entity
@NoArgsConstructor(access = PROTECTED)
@Table(
    name = "user_activity_histories",
    uniqueConstraints = {
      @UniqueConstraint(
          name = "UK_USER_ID_AND_GENERATION",
          columnNames = {"user_id", "generation"})
    })
public class UserActivityHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
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

  private UserActivityHistoryEntity(
      final UserEntity user,
      final int generation,
      final Team team,
      final Part part,
      final Role role) {
    this.user = user;
    this.generation = generation;
    this.team = team;
    this.part = part;
    this.role = role;
  }

  public static UserActivityHistoryEntity fromDomain(final User user, final Activity activity) {
    UserEntity userEntity = UserEntity.fromDomain(user);
    return new UserActivityHistoryEntity(
        userEntity,
        activity.generation(),
        activity.team().orElse(null),
        activity.part().orElse(null),
        activity.role());
  }

  public Activity toDomain() {
    return Activity.of(generation, team, part, role);
  }
}
