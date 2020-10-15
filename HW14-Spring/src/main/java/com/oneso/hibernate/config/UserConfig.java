package com.oneso.hibernate.config;

import com.oneso.hibernate.core.model.Address;
import com.oneso.hibernate.core.model.Phone;
import com.oneso.hibernate.core.model.User;
import com.oneso.hibernate.core.service.ServiceUser;
import com.oneso.hibernate.core.service.ServiceUserImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class UserConfig {

    @Bean
    public User initOnePerson(ServiceUserImpl serviceUser) {
        User user = new User();
        Address address = new Address(0, "street #123", user);
        Phone phone = new Phone(0, "phone #1", user);
        user.setName("John");
        user.setAddress(address);
        user.setPhones(List.of(phone));

        serviceUser.saveUser(user);
        return user;
    }
}
