package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

import java.time.LocalTime;

public class ProcessorThrowExceptionOnEvenSecond implements Processor {

  @Override
  public Message process(Message message) {
    int seconds = LocalTime.now().getSecond();
    if (seconds % 2 == 0) {
      RuntimeException lastException = new RuntimeException("Even second exception");
      saveState(seconds, lastException);
      throw lastException;
    }
    return message;
  }

  public void saveState(long lastProcessedTime, RuntimeException lastException) {
    ProcessorMemento processorMemento = new ProcessorMemento(lastProcessedTime, lastException);
    LogMemento.addLog(processorMemento);
  }
}
