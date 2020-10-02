package com.oneso.hibernate;

import com.oneso.hibernate.core.dao.UserDao;
import com.oneso.hibernate.core.model.Address;
import com.oneso.hibernate.core.model.Phone;
import com.oneso.hibernate.core.model.User;
import com.oneso.hibernate.core.service.ServiceUserImpl;
import com.oneso.hibernate.utils.HibernateUtils;
import com.oneso.hibernate.core.dao.UserDaoHibernate;
import com.oneso.hibernate.core.sessionmanager.SessionManagerHibernate;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

public class AppTestHibernate {

  private static final Logger logger = LoggerFactory.getLogger(AppTestHibernate.class);

  public static void main(String[] args) {

    SessionFactory sessionFactory = HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
        User.class, Address.class, Phone.class);

    SessionManagerHibernate sessionManager = new SessionManagerHibernate(sessionFactory);
    UserDao userDao = new UserDaoHibernate(sessionManager);
    ServiceUserImpl dbServiceUser = new ServiceUserImpl(userDao, true);

    User user = new User();
    Address address = new Address(0, "street #123", user);
    Phone phone = new Phone(0, "phone #1", user);
    user.setName("John");
    user.setAddress(address);
    user.setPhones(List.of(phone));

    long id = dbServiceUser.saveUser(user);
    Optional<User> mayBeCreatedUser = dbServiceUser.getUser(id);

    id = dbServiceUser.saveUser(new User(1L, "А! Нет. Это же совсем не Вася", address, List.of(phone)));
    Optional<User> mayBeUpdatedUser = dbServiceUser.getUser(id);
    dbServiceUser.clearListener();

    outputUserOptional("Created user", mayBeCreatedUser);
    outputUserOptional("Updated user", mayBeUpdatedUser);
  }

  private static void outputUserOptional(String header, Optional<User> mayBeUser) {
    System.out.println("-----------------------------------------------------------");
    System.out.println(header);
    mayBeUser.ifPresentOrElse(System.out::println, () -> logger.info("User not found"));
  }
}
