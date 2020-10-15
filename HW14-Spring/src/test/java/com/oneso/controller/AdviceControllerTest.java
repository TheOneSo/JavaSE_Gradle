package com.oneso.controller;

import com.oneso.hibernate.core.service.ServiceUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class AdviceControllerTest {

  private MockMvc mvc;

  @Mock
  private ServiceUser serviceUser;

  @BeforeEach
  void setUp() {
    mvc = MockMvcBuilders.standaloneSetup(new UserController(serviceUser))
        .setControllerAdvice(new AdviceController()).build();
  }

  @Test
  void getExceptionView() throws Exception {
    mvc.perform(get("/create"))
        .andExpect(status().isNotFound())
        .andReturn();
  }
}
