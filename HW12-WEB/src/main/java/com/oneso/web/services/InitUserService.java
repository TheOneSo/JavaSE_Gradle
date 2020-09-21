package com.oneso.web.services;

import com.oneso.hwhibernate.core.model.User;

public interface InitUserService {
    User initDefault();
    User init(String name, String address, String number);
}
