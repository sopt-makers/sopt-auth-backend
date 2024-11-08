package sopt.makers.authentication.database.core.entity;

import sopt.makers.authentication.domain.auth.Activity;
import sopt.makers.authentication.domain.auth.Part;
import sopt.makers.authentication.domain.auth.Role;
import sopt.makers.authentication.domain.auth.Team;

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
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_ACTIVITY_HISTORIES")
public class UserActivityHistoryEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  @NotNull
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

  public UserActivityHistoryEntity(final UserEntity user, final Activity activity) {
    this.user = user;
    this.generation = activity.generation();
    this.team = activity.team();
    this.part = activity.part();
    this.role = activity.role();
  }

  public Activity toDomain() {
    return new Activity(generation, team, part);
  }
}
