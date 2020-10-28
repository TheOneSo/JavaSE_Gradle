package com.oneso.util;

import com.oneso.hibernate.core.model.Address;
import com.oneso.hibernate.core.model.Phone;
import com.oneso.hibernate.core.model.User;

import java.util.List;

public class Util {

  public static User initUser() {
    User user = new User();
    Address address = new Address(0, "street #123", user);
    Phone phone = new Phone(0, "phone #1", user);
    user.setName("John");
    user.setId(1L);
    user.setAddress(address);
    user.setPhones(List.of(phone));
    return user;
  }
}
