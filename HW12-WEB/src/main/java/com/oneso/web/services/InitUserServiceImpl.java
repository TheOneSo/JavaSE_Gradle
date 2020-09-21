package com.oneso.web.services;

import com.oneso.hwhibernate.core.model.Address;
import com.oneso.hwhibernate.core.model.Phone;
import com.oneso.hwhibernate.core.model.User;

import java.util.List;

public class InitUserServiceImpl implements InitUserService {

    @Override
    public User initDefault() {
        User user = new User();
        user.setName("John");
        user.setAddress(new Address(0, "#123", user));
        user.setPhones(List.of(new Phone(0, "+7 123-456-00", user)));
        return user;
    }

    @Override
    public User init(String name, String address, String phone) {
        User user = new User();
        user.setName(name);
        user.setAddress(new Address(0, address, user));
        user.setPhones(List.of(new Phone(0, phone, user)));
        return user;
    }
}
