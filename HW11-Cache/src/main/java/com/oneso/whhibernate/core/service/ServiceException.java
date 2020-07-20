package com.oneso.whhibernate.core.service;

public class ServiceException extends RuntimeException {
  public ServiceException(Exception e) {
    super(e);
  }
}
