package sopt.makers.authentication.application.auth.event;

import sopt.makers.authentication.domain.auth.PhoneVerificationCreatedEvent;
import sopt.makers.authentication.domain.message.Message;
import sopt.makers.authentication.usecase.message.port.out.MessageSendPort;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PhoneVerificationEventListener {
  private final MessageSendPort messageSendPort;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handlePhoneVerificationCreated(PhoneVerificationCreatedEvent event) {
    Message message = Message.sms(event.phone(), event.content());

    messageSendPort.sendMessage(message);
  }
}
