package com.oneso.hibernate.core.model;

import javax.persistence.*;

@Entity
@Table(name = "addresses")
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  @Column(name = "street", nullable = false, unique = true)
  private String street;

  @JoinColumn(name = "user")
  @OneToOne(targetEntity = User.class, fetch = FetchType.LAZY)
  private User user;

  public Address() {}


  public Address(long id, String street, User user) {
    this.id = id;
    this.street = street;
    this.user = user;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  @Override
  public String toString() {
    return "Address{" +
        "id=" + id +
        ", street='" + street + '\'' +
        '}';
  }
}
