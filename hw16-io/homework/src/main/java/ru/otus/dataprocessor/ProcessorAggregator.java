package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ProcessorAggregator implements Processor {

  @Override
  public Map<String, Double> process(List<Measurement> data) {
    Map<String, Double> result = new TreeMap<>();
    for (Measurement measurement : data) {
      result.merge(measurement.name(), measurement.value(), Double::sum);
    }
    return result;
  }
}
