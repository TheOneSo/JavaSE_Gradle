package com.oneso.web.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

public class AuthServiceImpl implements AuthService {

  private static final Logger logger = LoggerFactory.getLogger(AuthServiceImpl.class);

  private static final Properties properties = new Properties();

  public AuthServiceImpl(Path file) {
    updateProperties(file);
  }

  @Override
  public boolean authenticate(String name, String pass) {
    return name.equals(properties.getProperty("login")) && pass.equals(properties.getProperty("pass"));
  }

  private void updateProperties(Path file) {
    try(InputStream inputStream = new FileInputStream(file.toFile())) {
      properties.load(inputStream);
    } catch (IOException ex) {
      logger.error("File has a problem. Message: {}", ex.getMessage());
    }
  }
}
