package com.oneso.hibernate.cache;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

public class MyCache<K, V> implements HwCache<K, V> {

  private static final Logger logger = LoggerFactory.getLogger(MyCache.class);
  private final List<WeakReference<HwListener<K, V>>> referencesListener = new ArrayList<>();
  private final Map<K, V> cache = new WeakHashMap<>();

  @Override
  public void put(K key, V value) {
    cache.put(key, value);

    callListeners(key, value, "put");
  }

  @Override
  public void remove(K key) {
    V value = cache.get(key);
    cache.remove(key);

    callListeners(key, value, "remove");
  }

  @Override
  public V get(K key) {
    V value = cache.get(key);

    callListeners(key, value, "get");
    return value;
  }

  @Override
  public List<V> getAll() {
    List<V> out = new ArrayList<>();
    for (var temp : cache.entrySet()) {
      out.add(temp.getValue());
    }
    return out;
  }

  @Override
  public void addListener(HwListener<K, V> listener) {
    addListener(listener, false);
  }

  @Override
  public void addListener(HwListener<K, V> listener, boolean controlRemove) {
    ReferenceQueue<HwListener<K, V>> referenceQueue = new ReferenceQueue<>();
    WeakReference<HwListener<K, V>> reference = new WeakReference<>(listener, referenceQueue);

    referencesListener.add(reference);

    if(controlRemove) {
      createThread(reference, referenceQueue);
    }
  }

  @Override
  public void removeListener(HwListener<K, V> listener) {
    for (WeakReference<HwListener<K, V>> temp : referencesListener) {
      if (temp.get() == listener) {
        temp.enqueue();
      }
    }
  }

  private void callListeners(K key, V value, String action) {
    for(WeakReference<HwListener<K, V>> temp : referencesListener) {
      var listener = temp.get();
      if(listener != null) {
        listener.notify(key, value, action);
      }
    }
  }

  private void createThread(WeakReference<HwListener<K, V>> reference, ReferenceQueue<HwListener<K, V>> referenceQueue) {
    new Thread(
        () -> {
          try {
            referenceQueue.remove();
            referencesListener.remove(reference);
            logger.info("Listener was removed");
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
        }
    ).start();
  }
}
