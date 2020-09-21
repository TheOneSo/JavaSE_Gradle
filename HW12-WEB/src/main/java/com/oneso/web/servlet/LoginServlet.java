package com.oneso.web.servlet;

import com.oneso.web.services.AuthService;
import com.oneso.web.services.TemplateProcessor;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Collections;

public class LoginServlet extends HttpServlet {

  private static final String LOGIN_NAME = "login";
  private static final String PASSWORD_NAME = "password";
  private static final int MAX_INACTIVE_INTERVAL = 30;
  private static final String LOGIN_PAGE = "login.html";

  private final TemplateProcessor templateProcessor;
  private final AuthService authService;

  public LoginServlet(TemplateProcessor templateProcessor, AuthService authService) {
    this.templateProcessor = templateProcessor;
    this.authService = authService;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    response.setContentType("text/html");
    response.getWriter().println(templateProcessor.getPage(LOGIN_PAGE, Collections.emptyMap()));
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
    String name = request.getParameter(LOGIN_NAME);
    String pass = request.getParameter(PASSWORD_NAME);

    if(authService.authenticate(name, pass)) {
      HttpSession session = request.getSession();
      session.setMaxInactiveInterval(MAX_INACTIVE_INTERVAL);
      response.sendRedirect("/users");
    } else {
      response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }
  }
}
