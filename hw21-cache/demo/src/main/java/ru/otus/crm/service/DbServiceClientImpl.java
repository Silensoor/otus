package ru.otus.crm.service;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.DataTemplate;
import ru.otus.core.sessionmanager.TransactionRunner;
import ru.otus.crm.model.Client;

public class DbServiceClientImpl implements DBServiceClient {
    private static final Logger log = LoggerFactory.getLogger(DbServiceClientImpl.class);
    private final HwCache<Long, Client> cache = new MyCache<>();
    private final DataTemplate<Client> dataTemplate;
    private final TransactionRunner transactionRunner;

    public DbServiceClientImpl(TransactionRunner transactionRunner, DataTemplate<Client> dataTemplate) {
        this.transactionRunner = transactionRunner;
        this.dataTemplate = dataTemplate;

    }

    private void addListener(){
      HwListener<Long,Client> hwListener = new HwListener<Long, Client>() {
        @Override
        public void notify(Long key, Client value, String action) {
          log.info("key: {}, value: {}, action: {}", key, value, action);
        }
      };
      cache.addListener(hwListener);
    }
  @Override
  public Client saveClient(Client client) {
    return transactionRunner.doInTransaction(connection -> {
      if (client.getId() == null) {
        var clientId = dataTemplate.insert(connection, client);
        var createdClient = new Client(clientId, client.getName());
        cache.put(clientId, createdClient); // Кэширование при создании
        log.info("created client: {}", createdClient);
        return createdClient;
      }
      dataTemplate.update(connection, client);
      cache.put(client.getId(), client); // Обновление кэша при изменении
      log.info("updated client: {}", client);
      return client;
    });
  }

  @Override
  public Optional<Client> getClient(long id) {
    Client client = cache.get(id);
    if (client != null) {
      log.info("client: {}", client);
      return Optional.of(client);
    }
    return transactionRunner.doInTransaction(connection -> {
      var clientOptional = dataTemplate.findById(connection, id);
      clientOptional.ifPresent(value -> cache.put(id, value)); // Кэширование при извлечении из БД
      log.info("client: {}", clientOptional);
      return clientOptional;
    });
  }

    @Override
    public List<Client> findAll() {
        return transactionRunner.doInTransaction(connection -> {
            var clientList = dataTemplate.findAll(connection);
            log.info("clientList:{}", clientList);
            return clientList;
        });
    }
}
