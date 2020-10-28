package com.oneso.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.oneso.hibernate.core.model.UserDTO;
import com.oneso.hibernate.core.service.ServiceUser;
import com.oneso.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class UserRestControllerTest {

  private MockMvc mvc;

  @Mock
  private ServiceUser serviceUser;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(new UserRestController(serviceUser)).build();
  }

  @Test
  void getUserById() throws Exception {
    var user = Util.initUser();
    var userDTO = new UserDTO(user);
    Gson gson = new GsonBuilder().create();
    given(serviceUser.getUser(1L)).willReturn(Optional.of(user));

    mvc.perform(get("/api/user/{id}", 1L))
        .andExpect(status().isOk())
        .andExpect(content().string(gson.toJson(userDTO)));
  }
}
