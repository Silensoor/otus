package ru.otus.dataprocessor;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import ru.otus.model.Measurement;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class ResourcesFileLoader implements Loader {

  private final String fileNAme;

  public ResourcesFileLoader(String fileName) {
    this.fileNAme = fileName;
  }

  @Override
  public List<Measurement> load() {
    List<Measurement> measurements = new ArrayList<>();

    try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileNAme);
         InputStreamReader reader = new InputStreamReader(inputStream)) {
      var gson = new Gson();
      return gson.fromJson(reader, new TypeToken<List<Measurement>>() {
      }.getType());
    } catch (Exception e) {
      throw new FileProcessException(e);
    }
  }
}
