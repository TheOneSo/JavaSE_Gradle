package com.oneso.web.services;

public interface AuthService {
  boolean authenticate(String name, String pass);
}
