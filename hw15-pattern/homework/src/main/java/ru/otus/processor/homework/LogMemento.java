package ru.otus.processor.homework;

import java.util.*;

public class LogMemento {
  private static final ArrayDeque<ProcessorMemento> mementos = new ArrayDeque<>();


  public static void addLog(ProcessorMemento processorMemento) {
    mementos.addFirst(processorMemento);
  }

  public static ArrayDeque<ProcessorMemento> getMementos() {
    return mementos;
  }

}
