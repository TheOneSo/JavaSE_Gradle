package com.oneso.web.servlet;

import com.oneso.web.helpers.HibernateHelper;
import com.oneso.web.services.TemplateProcessor;
import com.oneso.hwhibernate.core.service.ServiceUser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class NewUserServlet extends HttpServlet {

  private static final String PAGE_TEMPLATE = "create.html";
  private static final String FIELD_NAME = "name";
  private static final String FIELD_PHONE = "phone";
  private static final String FIELD_ADDRESS = "address";

  private final ServiceUser serviceUser;
  private final TemplateProcessor templateProcessor;

  public NewUserServlet(ServiceUser serviceUser, TemplateProcessor templateProcessor) {
    this.serviceUser = serviceUser;
    this.templateProcessor = templateProcessor;
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    resp.setContentType("text/html");
    resp.getWriter().println(templateProcessor.getPage(PAGE_TEMPLATE, Collections.emptyMap()));
  }

  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    String name = req.getParameter(FIELD_NAME);
    String phone = req.getParameter(FIELD_PHONE);
    String address = req.getParameter(FIELD_ADDRESS);

    serviceUser.saveUser(HibernateHelper.createUser(name, phone, address));
    resp.sendRedirect("/users");
  }
}
