package com.oneso.controller;

import com.oneso.hibernate.core.service.ServiceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

  private MockMvc mvc;
  private static final String USERS_VIEW = "users.html";
  private static final String CREATE_VIEW = "userCreate.html";

  @Mock
  private ServiceUser serviceUser;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(new UserController(serviceUser)).build();
  }

  @Test
  void getViewUsers() throws Exception {
    mvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name(USERS_VIEW))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
  }

  @Test
  void getViewCreateUser() throws Exception {
    mvc.perform(get("/user/create"))
        .andExpect(status().isOk())
        .andExpect(view().name(CREATE_VIEW))
        .andDo(MockMvcResultHandlers.print())
        .andReturn();
  }

  @Test
  void getHomeWhenSaveUser() throws Exception {
    mvc.perform(post("/user/save"))
        .andExpect(status().isFound())
        .andExpect(redirectedUrl("/"));
  }
}
