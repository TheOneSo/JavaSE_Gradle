package com.oneso.hwhibernate.core.service;

public class ServiceException extends RuntimeException {
  public ServiceException(Exception e) {
    super(e);
  }
}
