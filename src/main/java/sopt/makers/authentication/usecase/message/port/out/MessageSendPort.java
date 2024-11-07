package sopt.makers.authentication.usecase.message.port.out;

import sopt.makers.authentication.domain.message.Message;

public interface MessageSendPort {
  void sendMessage(Message message);
}
