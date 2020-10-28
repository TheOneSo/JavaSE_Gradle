package com.oneso.hibernate.cache;

import java.util.List;

public interface HwCache<K, V> {

  void put(K key, V value);

  void remove(K key);

  V get(K key);

  List<V> getAll();

  void addListener(HwListener<K, V> listener);

  void addListener(HwListener<K, V> listener, boolean controlRemove);

  void removeListener(HwListener<K, V> listener);
}
