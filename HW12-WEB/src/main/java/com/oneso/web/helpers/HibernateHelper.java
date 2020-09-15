package com.oneso.web.helpers;

import com.oneso.hwhibernate.core.dao.UserDao;
import com.oneso.hwhibernate.core.model.Address;
import com.oneso.hwhibernate.core.model.Phone;
import com.oneso.hwhibernate.core.model.User;
import com.oneso.hwhibernate.core.service.ServiceUser;
import com.oneso.hwhibernate.core.service.ServiceUserImpl;
import com.oneso.hwhibernate.hibernate.HibernateUtils;
import com.oneso.hwhibernate.hibernate.dao.UserDaoHibernate;
import com.oneso.hwhibernate.hibernate.sessionmanager.SessionManagerHibernate;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public final class HibernateHelper {

  private static final Logger logger = LoggerFactory.getLogger(HibernateHelper.class);

  public static ServiceUser createServiceUser() {
    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
        User.class, Address.class, Phone.class);

    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    UserDao userDao = new UserDaoHibernate(sessionManager);
    ServiceUser serviceUser = new ServiceUserImpl(userDao, true);
    serviceUser.saveUser(createUser("John", "+7 123-456-00", "#123"));

    return serviceUser;
  }

  public static User createUser(String name, String phone, String address) {
    User user = new User();
    user.setName(name);
    user.setAddress(new Address(0, address, user));
    user.setPhones(List.of(new Phone(0, phone, user)));
    logger.info("User [{}] was created", user.getName());
    return user;
  }
}
