package com.oneso;

import com.oneso.core.model.Account;
import com.oneso.core.service.DbServiceAccountImpl;
import com.oneso.h2.DataSourceH2;
import com.oneso.jdbc.*;
import com.oneso.jdbc.sessionmanager.SessionManagerJdbc;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Db Service Demo should test new Object")
class DbServiceDemoTest {

  private DbServiceAccountImpl dbServiceAccount;

  @BeforeEach
  void setUp() throws SQLException {
    var dataSource = new DataSourceH2();
    var demo = new DbServiceDemoTest();

    demo.createTableAccount(dataSource);

    var sessionManager = new SessionManagerJdbc(dataSource);
    DbExecutor<Account> dbExecutor = new DbExecutorImpl<>();
    var entityClassMetaData = new EntityClassMetaDataImpl<>(Account.class);
    var entitySQLMetaData = new EntitySQLMetaDataImpl<>(entityClassMetaData);
    var jdbcMapper = new JdbcMapperImpl<>(sessionManager, dbExecutor, entityClassMetaData, entitySQLMetaData);

    dbServiceAccount = new DbServiceAccountImpl(jdbcMapper);
  }

  @Test
  @DisplayName("should create and to get account")
  void testAccount() {
    Account account = new Account(0, "test", new BigDecimal(5));
    var accId = dbServiceAccount.saveAccount(new Account(0, "test", new BigDecimal(5)));
    Optional<Account> acc = dbServiceAccount.getAccount(accId);

    assertEquals(account.getType(), acc.get().getType());
  }

  private void createTableAccount(DataSource dataSource) throws SQLException {
    try (var connection = dataSource.getConnection();
         var pst = connection.prepareStatement("create table Account(id bigint(20) NOT NULL auto_increment, type varchar(255), rest number)")) {
      pst.executeUpdate();
    }
  }

}
