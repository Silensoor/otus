package ru.otus.listener.homework;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import java.time.LocalTime;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;
import ru.otus.model.ObjectForMessage;
import ru.otus.processor.Processor;
import ru.otus.processor.homework.LogMemento;
import ru.otus.processor.homework.ProcessorMemento;
import ru.otus.processor.homework.ProcessorThrowExceptionOnEvenSecond;

class HistoryListenerTest {

  @Test
  void listenerTest() {
    // given
    var historyListener = new HistoryListener();

    var id = 100L;
    var data = "33";
    var field13 = new ObjectForMessage();
    var field13Data = new ArrayList<String>();
    field13Data.add(data);
    field13.setData(field13Data);

    var message = new Message.Builder(id)
      .field10("field10")
      .field13(field13)
      .build();

    // when
    historyListener.onUpdated(message);
    //message.getField13().setData(new ArrayList<>()); //меняем исходное сообщение
    //field13Data.clear(); //меняем исходный список

    // then
    var messageFromHistory = historyListener.findMessageById(id);
    assertThat(messageFromHistory).isPresent();
    assertThat(messageFromHistory.get().getField13().getData()).containsExactly(data);
  }

  @Test
  void complexProcessorTest() throws InterruptedException {
    List<Processor> processors = List.of(new ProcessorThrowExceptionOnEvenSecond());
    var complexProcessor = new ComplexProcessor(processors, ex -> {
    });

    var message = new Message.Builder(1L)
      .field1("field1")
      .field2("field2")
      .field3("field3")
      .field6("field6")
      .field10("field10")
      .build();

    complexProcessor.handle(message);

    int seconds = LocalTime.now().getSecond();

    if (LogMemento.getMementos().isEmpty()) {
      assertThat(seconds % 2).isNotEqualTo(0);
    } else {
      ArrayDeque<ProcessorMemento> mementos = LogMemento.getMementos();
      ProcessorMemento first = mementos.getFirst();
      assertThat(first.lastProcessedTime() % 2).isEqualTo(0);
    }
  }
}
