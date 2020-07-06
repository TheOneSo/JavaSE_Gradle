package com.oneso.core.model;

import com.oneso.jdbc.mapper.Id;

public class UserJdbc {

  @Id
  public long id;

  public String name;

  public int age;

  public UserJdbc(long id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public UserJdbc(){}

  public int getAge() {
    return age;
  }

  public String getName() {
    return name;
  }

  public long getId() {
    return id;
  }
}
