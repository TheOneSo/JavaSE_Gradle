package com.oneso.hibernate.exceptions;

public class SessionManagerException extends RuntimeException {
  public SessionManagerException(String msg) {
    super(msg);
  }
  public SessionManagerException(Exception ex) {
    super(ex);
  }
}
