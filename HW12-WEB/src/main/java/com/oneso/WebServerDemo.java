package com.oneso;

import com.oneso.web.helpers.HibernateHelper;
import com.oneso.web.server.UsersWebServer;
import com.oneso.web.server.UsersWebServerImpl;
import com.oneso.web.services.AuthService;
import com.oneso.web.services.AuthServiceImpl;
import com.oneso.web.services.TemplateProcessor;
import com.oneso.web.services.TemplateProcessorImpl;

import java.nio.file.Path;

public class WebServerDemo {
  private static final int WEB_SERVER_PORT = 8080;
  private static final String TEMPLATES_DIR = "/templates/";
  private static final String LOGIN_SERVICE_CONFIG_NAME = "conf.properties";

  public static void main(String[] args) throws Exception {
    TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
    AuthService authService = new AuthServiceImpl(Path.of(ClassLoader.getSystemResource(LOGIN_SERVICE_CONFIG_NAME).toURI()));

    UsersWebServer usersWebServer = new UsersWebServerImpl(WEB_SERVER_PORT, templateProcessor, authService, HibernateHelper.createServiceUser());

    usersWebServer.start();
    usersWebServer.join();
  }
}
