package sopt.makers.authentication.domain.auth;

import sopt.makers.authentication.domain.message.MessageType;

public record PhoneVerificationCreatedEvent(String phone, String code, MessageType type) {}
