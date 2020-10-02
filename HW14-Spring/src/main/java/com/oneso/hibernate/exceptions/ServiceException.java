package com.oneso.hibernate.exceptions;

public class ServiceException extends RuntimeException {
  public ServiceException(Exception e) {
    super(e);
  }
}
