package ru.otus.listener.homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

public class HistoryListener implements Listener, HistoryReader {
  private final List<Message> history = new ArrayList<>();

  @Override
  public void onUpdated(Message msg) {
    history.add(msg.toBuilder().build());
  }

  @Override
  public Optional<Message> findMessageById(long id) {
    return history.stream()
      .filter(msg -> msg.getId() == id)
      .findFirst()
      .map(Message::toBuilder)
      .map(Message.Builder::build);
  }
}
