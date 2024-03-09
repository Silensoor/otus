package ru.otus.dataprocessor;

import com.google.gson.JsonParser;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import ru.otus.model.Measurement;

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

    try (JsonReader jsonReader = Json.createReader(JsonParser.class.getClassLoader().getResourceAsStream(fileNAme))) {
      JsonArray jsonArray = jsonReader.readArray();
      for (JsonObject jsonObject : jsonArray.getValuesAs(JsonObject.class)) {
        String name = jsonObject.getString("name");
        double value = jsonObject.getJsonNumber("value").doubleValue();
        measurements.add(new Measurement(name, value));
      }
      return measurements;
    } catch (Exception e) {
      throw new FileProcessException(e);
    }
  }
}
