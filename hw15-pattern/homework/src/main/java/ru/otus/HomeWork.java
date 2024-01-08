package ru.otus;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.LoggerProcessor;
import ru.otus.processor.ProcessorConcatFields;
import ru.otus.processor.ProcessorUpperField10;
import ru.otus.processor.homework.ProcessorSwapFields;
import ru.otus.processor.homework.ProcessorThrowExceptionOnEvenSecond;

import java.util.List;

public class HomeWork {
  private static final Logger logger = LoggerFactory.getLogger(HomeWork.class);

  public static void main(String[] args) {
    var processors = List.of(
      new ProcessorConcatFields(),
      new LoggerProcessor(new ProcessorUpperField10()),
      new ProcessorSwapFields(),
      new ProcessorThrowExceptionOnEvenSecond()
    );
    var complexProcessor = new ComplexProcessor(processors, ex -> {
    });
    var historyListener = new HistoryListener();
    complexProcessor.addListener(historyListener);
    var message = new Message.Builder(1L)
      .field1("field1")
      .field2("field2")
      .field3("field3")
      .field6("field6")
      .field10("field10")
      .field11("field11")
      .field12("field12")
      .field13(new ObjectForMessage())
      .build();

    var result = complexProcessor.handle(message);
    logger.info("result: {}", result);

    var messageFromHistory = historyListener.findMessageById(1L);
    logger.info("messageFromHistory: {}", messageFromHistory.orElse(null));

    complexProcessor.removeListener(historyListener);

  }
}
