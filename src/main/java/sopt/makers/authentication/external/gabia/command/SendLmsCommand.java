package sopt.makers.authentication.external.gabia.command;

public record SendLmsCommand(String receiver, String title, String content)
    implements GabiaCommand {}
