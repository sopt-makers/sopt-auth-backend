package sopt.makers.authentication.application.port.out.message;

import sopt.makers.authentication.domain.message.Message;

public interface MessageSendPort {
  void sendMessage(Message message);
}
