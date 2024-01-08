package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorThrowExceptionOnEvenSecond implements Processor {

  @Override
  public Message process(Message message) {
    if (System.currentTimeMillis() % 2 == 0) {
      throw new RuntimeException("Even second exception");
    }
    return message;
  }
}
