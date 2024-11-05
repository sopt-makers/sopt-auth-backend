package sopt.makers.authentication.external.gabia.command;

public record SendSmsCommand(String receiver, String message) implements GabiaCommand {}
