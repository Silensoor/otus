package ru.homework.otus;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        Counter counter = new Counter();

        executorService.execute(counter);
        executorService.execute(counter);

        executorService.shutdown();
    }
}
