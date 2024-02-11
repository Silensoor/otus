package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySQLMetaDataImpl;

import javax.sql.DataSource;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWork {
  private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
  private static final String USER = "usr";
  private static final String PASSWORD = "pwd";

  private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

  public static void main(String[] args) {
    // Общая часть
    var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
    flywayMigrations(dataSource);
    var transactionRunner = new TransactionRunnerJdbc(dataSource);
    var dbExecutor = new DbExecutorImpl();

    // Работа с клиентом
    EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
    EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
    var dataTemplateClient = new DataTemplateJdbc<Client>(
      dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient); // реализация DataTemplate, универсальная

    // Код дальше должен остаться
    var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);

    int x = 0;
    while (x != 50000) {
      dbServiceClient.saveClient(new Client("dbServiceFirst"));
      x++;
    }

    var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
    long oldTime = System.currentTimeMillis();


    var clientSecondSelected = dbServiceClient
      .getClient(clientSecond.getId())
      .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));


    log.info(String.format("Время выполнения: %d", (System.currentTimeMillis() - oldTime)));
  }

  private static void flywayMigrations(DataSource dataSource) {
    log.info("db migration started...");
    var flyway = Flyway.configure()
      .dataSource(dataSource)
      .locations("classpath:/db/migration")
      .load();
    flyway.migrate();
    log.info("db migration finished.");
    log.info("***");
  }
}
