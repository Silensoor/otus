package ru.otus.services.processors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.api.SensorDataProcessor;
import ru.otus.api.model.SensorData;
import ru.otus.lib.SensorDataBufferedWriter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;

@SuppressWarnings({"java:S1068", "java:S125"})
public class SensorDataProcessorBuffered implements SensorDataProcessor {
    private static final Logger log = LoggerFactory.getLogger(SensorDataProcessorBuffered.class);

    private final int bufferSize;
    private final ArrayBlockingQueue<SensorData> dataBuffer;
    private final SensorDataBufferedWriter writer;

    public SensorDataProcessorBuffered(int bufferSize, SensorDataBufferedWriter writer) {
        this.bufferSize = bufferSize;
        this.writer = writer;
        this.dataBuffer = new ArrayBlockingQueue<>(bufferSize);
    }

    @Override
    public synchronized void process(SensorData data) {
        try {
            dataBuffer.put(data);
            if (dataBuffer.size() >= bufferSize) {
                flush();
            }
        } catch (InterruptedException e) {
            log.error("Interrupted while putting data into buffer", e);
            Thread.currentThread().interrupt();
        }
    }

    public synchronized void flush() {
        if (!dataBuffer.isEmpty()) {
            List<SensorData> bufferedData = new ArrayList<>(dataBuffer);
            bufferedData.sort(Comparator.comparing(SensorData::getMeasurementTime));
            try {
                writer.writeBufferedData(bufferedData);
            } catch (Exception e) {
                log.error("Ошибка в процессе записи буфера", e);
            } finally {
                dataBuffer.clear();
            }
        }

    }

    @Override
    public void onProcessingEnd() {
        flush();
    }
}
