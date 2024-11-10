package sopt.makers.authentication.database.core.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "phone_verifications")
public class PhoneVerification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;
}
