package ru.otus.processor.homework;

public record ProcessorMemento(Long lastProcessedTime, RuntimeException lastException) {
}
