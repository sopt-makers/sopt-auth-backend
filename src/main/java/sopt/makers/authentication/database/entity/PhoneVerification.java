package sopt.makers.authentication.database.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class PhoneVerification {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;
}
