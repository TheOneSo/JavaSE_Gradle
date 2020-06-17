package com.oneso;

import java.sql.SQLException;
import java.util.Optional;
import javax.sql.DataSource;

import com.oneso.core.service.DbServiceUserImpl;
import com.oneso.jdbc.*;
import com.oneso.jdbc.sessionmanager.SessionManagerJdbc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.oneso.h2.DataSourceH2;
import com.oneso.core.model.User;

/**
 * @author sergey
 * created on 03.02.19.
 */
public class DbServiceDemo {
  private static final Logger logger = LoggerFactory.getLogger(DbServiceDemo.class);

  public static void main(String[] args) throws Exception {
    var dataSource = new DataSourceH2();
    var demo = new DbServiceDemo();

    demo.createTableUser(dataSource);

    var sessionManager = new SessionManagerJdbc(dataSource);
    DbExecutor<User> dbExecutor = new DbExecutorImpl<>();
    var entityClassMetaData = new EntityClassMetaDataImpl<>(User.class);
    var entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
    var jdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, entityClassMetaData, entitySQLMetaData);

    var dbServiceUser = new DbServiceUserImpl(jdbcMapper);
    var id = dbServiceUser.saveUser(new User(0, "Igor", 20));
    Optional<User> user = dbServiceUser.getUser(id);

    user.ifPresentOrElse(
        crUser -> logger.info("created user, name: {}", crUser.getName()),
        () -> logger.info("user was not created")
    );

    var id2 = dbServiceUser.saveUser(new User(1, "John", 25));
    Optional<User> user2 = dbServiceUser.getUser(id2);

    user2.ifPresentOrElse(
        crUser -> logger.info("created user, name: {}", crUser.getName()),
        () -> logger.info("user was not created")
    );
  }

  private void createTableUser(DataSource dataSource) throws SQLException {
    try (var connection = dataSource.getConnection();
         var pst = connection.prepareStatement("create table User(id bigint(20) NOT NULL auto_increment, name varchar(255), age int(3))")) {
      pst.executeUpdate();
    }
    logger.info("Table was created");
  }
}
