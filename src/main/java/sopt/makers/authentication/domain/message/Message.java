package sopt.makers.authentication.domain.message;

import static lombok.AccessLevel.PRIVATE;

import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@Builder(access = PRIVATE)
@RequiredArgsConstructor(access = PRIVATE)
public class Message {
  private final String receiver;
  private final String title;
  private final String content;
  private final List<String> images;
  private final MessageType type;

  public static Message sms(String receiver, String content) {
    return Message.builder().receiver(receiver).content(content).type(MessageType.SMS).build();
  }

  public static Message lms(String receiver, String title, String content) {
    return Message.builder()
        .receiver(receiver)
        .title(title)
        .content(content)
        .type(MessageType.LMS)
        .build();
  }
}
