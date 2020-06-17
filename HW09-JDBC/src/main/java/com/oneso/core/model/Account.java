package com.oneso.core.model;

import com.oneso.jdbc.mapper.Id;

import java.math.BigDecimal;

public class Account {

  @Id
  public long id;

  public String type;

  public BigDecimal rest;

  public Account(long id, String type, BigDecimal rest) {
    this.id = id;
    this.type = type;
    this.rest = rest;
  }

  public long getId() {
    return id;
  }

  public String getType() {
    return type;
  }

  public BigDecimal getRest() {
    return rest;
  }
}
