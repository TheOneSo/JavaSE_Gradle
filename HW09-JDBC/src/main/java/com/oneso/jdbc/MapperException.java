package com.oneso.jdbc;

public class MapperException extends RuntimeException {
  public MapperException(String msg, Exception ex) {
    super(msg, ex);
  }
}
