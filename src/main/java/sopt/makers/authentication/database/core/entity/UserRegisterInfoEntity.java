package sopt.makers.authentication.database.core.entity;

import sopt.makers.authentication.domain.auth.*;

import java.time.*;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER_REGISTER_INFOS")
public class UserRegisterInfoEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotNull private String name;
  @NotNull private String phone;
  @NotNull private LocalDate birthday;

  @Min(1)
  private int generation;

  @NotNull
  @Enumerated(EnumType.STRING)
  private Part part;
}
