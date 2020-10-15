package com.oneso.controller;

import com.oneso.hibernate.core.model.User;
import com.oneso.hibernate.core.model.UserDTO;
import com.oneso.hibernate.core.service.ServiceUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class UserController {

    private static final String USERS_VIEW = "users.html";
    private static final String CREATE_VIEW = "userCreate.html";

    private final ServiceUser serviceUser;

    public UserController(ServiceUser serviceUser) {
        this.serviceUser = serviceUser;
    }

    @GetMapping({"/", "/users"})
    public String usersView(Model model) {
        List<User> userList = serviceUser.getUsers();
        List<UserDTO> userDTOS = userList.stream()
                .map(UserDTO::new)
                .collect(Collectors.toList());
        model.addAttribute("users", userDTOS);
        return USERS_VIEW;
    }

    @GetMapping("/user/create")
    public String createUserView(Model model) {
        model.addAttribute("user", new UserDTO());
        return CREATE_VIEW;
    }

    @PostMapping("/user/save")
    public RedirectView saveUser(@ModelAttribute UserDTO user) {
        serviceUser.saveUser(user.getUser());
        return new RedirectView("/", true);
    }
}
