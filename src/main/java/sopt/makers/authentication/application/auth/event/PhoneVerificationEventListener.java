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

  private static final String FORMAT_VERIFICATION_MESSAGE = "[SOPT makers]\n인증번호 [%s]를 입력해주세요.";

  private final MessageSendPort messageSendPort;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handlePhoneVerificationCreated(PhoneVerificationCreatedEvent event) {
    String content = String.format(FORMAT_VERIFICATION_MESSAGE, event.code());

    Message message = Message.sms(event.phone(), content);

    messageSendPort.sendMessage(message);
  }
}
