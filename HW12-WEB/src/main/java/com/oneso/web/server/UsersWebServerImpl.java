package com.oneso.web.server;

import com.oneso.web.helpers.FileSystemHelper;
import com.oneso.web.services.AuthService;
import com.oneso.web.services.InitUserService;
import com.oneso.web.services.TemplateProcessor;
import com.oneso.web.servlet.AuthorizationFilter;
import com.oneso.web.servlet.LoginServlet;
import com.oneso.web.servlet.NewUserServlet;
import com.oneso.web.servlet.UsersServlet;
import com.oneso.hwhibernate.core.service.ServiceUser;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class UsersWebServerImpl implements UsersWebServer {

  private static final Logger logger = LoggerFactory.getLogger(UsersWebServerImpl.class);

  private static final String START_PAGE_NAME = "index.html";
  private static final String COMMON_RESOURCES_DIR = "static";

  private final Server server;
  private final TemplateProcessor templateProcessor;
  private final AuthService authService;
  private final ServiceUser serviceUser;
  private final InitUserService initUserService;

  public UsersWebServerImpl(int port, TemplateProcessor templateProcessor, AuthService authService,
                            ServiceUser serviceUser, InitUserService initUserService) {
    this.serviceUser = serviceUser;
    this.initUserService = initUserService;
    this.templateProcessor = templateProcessor;
    this.authService = authService;
    this.server = new Server(port);
  }

  @Override
  public void start() throws Exception {
    if(server.getHandlers().length == 0) {
      initContext();
    }

    server.start();
  }

  @Override
  public void join() throws Exception {
    server.join();
  }

  @Override
  public void stop() throws Exception {
    server.stop();
  }

  private Server initContext() {
    ResourceHandler resourceHandler = createResourceHandler();
    ServletContextHandler servletContextHandler = createServletContextHandler();

    HandlerList handlerList = new HandlerList();
    handlerList.addHandler(resourceHandler);
    handlerList.addHandler(applySecurity(servletContextHandler, "/users", "/users/create"));

    server.setHandler(handlerList);
    return server;
  }

  private Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
    servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
    var authorizationFilter = new AuthorizationFilter();
    Arrays.stream(paths).forEachOrdered(path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
    return servletContextHandler;
  }

  private ResourceHandler createResourceHandler() {
    ResourceHandler resourceHandler = new ResourceHandler();
    resourceHandler.setDirectoriesListed(false);
    resourceHandler.setWelcomeFiles(new String[]{START_PAGE_NAME});
    resourceHandler.setResourceBase(FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
    return resourceHandler;
  }

  private ServletContextHandler createServletContextHandler() {
    ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
    servletContextHandler.addServlet(new ServletHolder(new UsersServlet(serviceUser, templateProcessor)), "/users");
    servletContextHandler.addServlet(new ServletHolder(
            new NewUserServlet(serviceUser, templateProcessor, initUserService)), "/users/create");
    return servletContextHandler;
  }
}
