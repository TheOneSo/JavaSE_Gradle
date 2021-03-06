package com.oneso.cache;

/**
 * @author sergey
 * created on 14.12.18.
 */
public interface HwCache<K, V> {

  void put(K key, V value);

  void remove(K key);

  V get(K key);

  void addListener(HwListener<K, V> listener);

  void addListener(HwListener<K, V> listener, boolean controlRemove);

  void removeListener(HwListener<K, V> listener);
}
