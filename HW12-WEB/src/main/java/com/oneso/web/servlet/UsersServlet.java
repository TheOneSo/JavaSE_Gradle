package com.oneso.web.servlet;

import com.oneso.web.services.TemplateProcessor;
import com.oneso.hwhibernate.core.service.ServiceUser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class UsersServlet extends HttpServlet {

  private static final String USERS_PAGE_TEMPLATE = "users.html";
  private static final String USERS_NAME = "users";

  private final ServiceUser serviceUser;
  private final TemplateProcessor templateProcessor;

  public  UsersServlet(ServiceUser serviceUser, TemplateProcessor templateProcessor) {
    this.serviceUser = serviceUser;
    this.templateProcessor = templateProcessor;
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    Map<String, Object> params = new HashMap<>();
    params.put(USERS_NAME, serviceUser.getUsers());

    response.setContentType("text/html");
    response.getWriter().println(templateProcessor.getPage(USERS_PAGE_TEMPLATE, params));
  }
}
