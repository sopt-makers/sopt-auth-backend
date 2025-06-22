package sopt.makers.authentication.adapter.out.external.sms;

import sopt.makers.authentication.domain.message.Message;
import sopt.makers.authentication.usecase.message.port.out.MessageSendPort;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSender implements MessageSendPort {

  private final GabiaClient gabiaClient;

  @Override
  public void sendMessage(Message message) {
    switch (message.getType()) {
      case SMS -> gabiaClient.sendSmsMessage(message.getReceiver(), message.getContent());
      case LMS -> gabiaClient.sendLmsMessage(
          message.getReceiver(), message.getTitle(), message.getContent());
    }
  }
}
