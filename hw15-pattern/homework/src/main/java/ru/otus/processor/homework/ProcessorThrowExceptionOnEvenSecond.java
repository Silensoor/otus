package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowExceptionOnEvenSecond implements Processor {
  private final DateTimeProvider dateTimeProvider;

  public ProcessorThrowExceptionOnEvenSecond(DateTimeProvider dateTimeProvider) {
    this.dateTimeProvider = dateTimeProvider;
  }

  public ProcessorThrowExceptionOnEvenSecond() {
    dateTimeProvider = new DefaultDateTimeProvider();
  }

  @Override
  public Message process(Message message) {
    if (isEvenSecond()) {
      throw new RuntimeException("Exception in even second");
    }
    return message;
  }

  private boolean isEvenSecond() {
    return dateTimeProvider.getCurrentTime().getSecond() % 2 == 0;
  }
}
