package com.oneso.hibernate.core.model;

import java.util.List;

public class UserDTO {

    private long id;

    private String name;

    private String address;

    private String phone;

    public UserDTO() {}

    public UserDTO(long id, String name, String address, String phone) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
    }

    public UserDTO(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.address = user.getAddress().getStreet();
        this.phone = user.getPhones().stream().findFirst().orElse(new Phone()).getName();
    }

    public User getUser() {
        User user = new User();
        user.setName(name);
        user.setAddress(new Address(0, address, user));
        user.setPhones(List.of(new Phone(0, phone, user)));
        return user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
