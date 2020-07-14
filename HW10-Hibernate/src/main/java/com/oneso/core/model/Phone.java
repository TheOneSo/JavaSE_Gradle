package com.oneso.core.model;

import javax.persistence.*;

@Entity
@Table(name = "phones")
public class Phone {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "name", nullable = false)
  private String name;

  @JoinColumn(name = "user")
  @ManyToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  private User user;

  public Phone() {}

  public Phone(long id, String name, User user) {
    this.id = id;
    this.name = name;
    this.user = user;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "Phone{" +
        "id=" + id +
        ", name='" + name + '\'' +
        '}';
  }
}
