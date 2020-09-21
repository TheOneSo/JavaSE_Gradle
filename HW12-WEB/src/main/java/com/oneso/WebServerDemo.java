package com.oneso;

import com.oneso.hwhibernate.core.dao.UserDao;
import com.oneso.hwhibernate.core.model.Address;
import com.oneso.hwhibernate.core.model.Phone;
import com.oneso.hwhibernate.core.model.User;
import com.oneso.hwhibernate.core.service.ServiceUser;
import com.oneso.hwhibernate.core.service.ServiceUserImpl;
import com.oneso.hwhibernate.hibernate.HibernateUtils;
import com.oneso.hwhibernate.hibernate.dao.UserDaoHibernate;
import com.oneso.hwhibernate.hibernate.sessionmanager.SessionManagerHibernate;
import com.oneso.web.server.UsersWebServer;
import com.oneso.web.server.UsersWebServerImpl;
import com.oneso.web.services.*;
import org.hibernate.SessionFactory;

import java.nio.file.Path;

public class WebServerDemo {
  private static final int WEB_SERVER_PORT = 8080;
  private static final String TEMPLATES_DIR = "/templates/";
  private static final String LOGIN_SERVICE_CONFIG_NAME = "conf.properties";

  public static void main(String[] args) throws Exception {
    TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);
    AuthService authService = new AuthServiceImpl(Path.of(ClassLoader.getSystemResource(LOGIN_SERVICE_CONFIG_NAME).toURI()));
    InitUserService initUserService = new InitUserServiceImpl();

    UsersWebServer usersWebServer = new UsersWebServerImpl(WEB_SERVER_PORT, templateProcessor,
            authService, createServiceUser(initUserService), initUserService);

    usersWebServer.start();
    usersWebServer.join();
  }

  private static ServiceUser createServiceUser(InitUserService initUserService) {
    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
            User.class, Address.class, Phone.class);

    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    UserDao userDao = new UserDaoHibernate(sessionManager);
    ServiceUser serviceUser = new ServiceUserImpl(userDao, true);
    serviceUser.saveUser(initUserService.initDefault());

    return serviceUser;
  }
}
