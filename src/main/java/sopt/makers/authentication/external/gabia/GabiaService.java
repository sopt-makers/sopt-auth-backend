package sopt.makers.authentication.external.gabia;

import sopt.makers.authentication.external.gabia.command.GabiaCommand;
import sopt.makers.authentication.external.gabia.command.SendLmsCommand;
import sopt.makers.authentication.external.gabia.command.SendSmsCommand;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GabiaService {

  private final GabiaClient gabiaClient;

  public void sendMessageTo(GabiaCommand command, MessageSendType sendType) {
    switch (sendType) {
      case SMS -> sendSMS((SendSmsCommand) command);
      case LMS -> sendLMS((SendLmsCommand) command);
    }
  }

  private void sendSMS(SendSmsCommand smsCommand) {
    gabiaClient.sendSmsMessage(smsCommand.receiver(), smsCommand.message());
  }

  private void sendLMS(SendLmsCommand lmsCommand) {
    gabiaClient.sendLmsMessage(lmsCommand.receiver(), lmsCommand.title(), lmsCommand.content());
  }
}
