package com.oneso.controller;

import com.oneso.hibernate.core.model.User;
import com.oneso.hibernate.core.model.UserDTO;
import com.oneso.hibernate.core.service.ServiceUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserRestController {

    private final ServiceUser serviceUser;

    public UserRestController(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    @GetMapping("/api/user/{id}")
    public UserDTO getUserById(@PathVariable(name = "id") long id) {
        Optional<User> optionalUser = serviceUser.getUser(id);
        return optionalUser.map(UserDTO::new).orElse(null);
    }
}
