package ru.otus.cachehw;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

  private final Map<K, V> cache = new WeakHashMap<>();
  private final Map<HwListener<K, V>, WeakReference<HwListener<K, V>>> listeners = new ConcurrentHashMap<>();

  @Override
  public void put(K key, V value) {
    cache.put(key, value);
    notifyListeners(key, value, "put");
  }

  @Override
  public void remove(K key) {
    V value = cache.remove(key);
    notifyListeners(key, value, "remove");
  }

  @Override
  public V get(K key) {
    return cache.get(key);
  }

  @Override
  public void addListener(HwListener<K, V> listener) {
    listeners.put(listener, new WeakReference<>(listener));
  }

  @Override
  public void removeListener(HwListener<K, V> listener) {
    listeners.remove(listener);
  }

  private void notifyListeners(K key, V value, String action) {
    listeners.forEach((listener, weakRef) -> {
      HwListener<K, V> listenerRef = weakRef.get();
      if (listenerRef != null) {
        listenerRef.notify(key, value, action);
      } else {
        listeners.remove(listener);
      }
    });
  }
}
