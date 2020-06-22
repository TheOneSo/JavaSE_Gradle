package com.oneso.core.sessionmanager;

import java.sql.Connection;

public interface DatabaseSession {
  Connection getConnection();
}
