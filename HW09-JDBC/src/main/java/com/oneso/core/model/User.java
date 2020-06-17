package com.oneso.core.model;

import com.oneso.jdbc.mapper.Id;

public class User {

  @Id
  public long id;

  public String name;

  public int age;

  public User(long id, String name, int age) {
    this.id = id;
    this.name = name;
    this.age = age;
  }

  public User(){}

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
