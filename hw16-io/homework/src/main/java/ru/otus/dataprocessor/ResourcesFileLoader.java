package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ResourcesFileLoader implements Loader {

  private final String fileNAme;

  public ResourcesFileLoader(String fileName) {
    this.fileNAme = fileName;
  }

  @Override
  public List<Measurement> load() {
    try {
      var filePath = Paths.get(ClassLoader.getSystemResource(fileNAme).toURI());
      Reader reader = Files.newBufferedReader(filePath);
      var gson = new Gson();
      return gson.fromJson(reader, new TypeToken<List<Measurement>>() {
      }.getType());
    } catch (Exception e) {
      throw new FileProcessException(e);
    }
  }
}
