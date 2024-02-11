package ru.otus.dataprocessor;

import com.google.gson.Gson;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class FileSerializer implements Serializer {
    private final String fileName;

    public FileSerializer(String fileName) {
    this.fileName = fileName;
    }

    @Override
    public void serialize(Map<String, Double> data) {
      try {
        var gson = new Gson();
        var jsonString = gson.toJson(data);
        Files.writeString(Paths.get(fileName), jsonString);
      } catch (Exception e) {
        throw new FileProcessException(e);
      }
    }
}
