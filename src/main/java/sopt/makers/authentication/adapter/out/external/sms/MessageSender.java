package sopt.makers.authentication.adapter.out.external.sms;

import sopt.makers.authentication.application.port.out.message.MessageSendPort;
import sopt.makers.authentication.domain.message.Message;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MessageSender implements MessageSendPort {

  private final SmsProxyClient smsProxyClient;

  @Override
  public void sendMessage(Message message) {
    smsProxyClient.sendSMS(message.getReceiver(), message.getContent());
  }
}
