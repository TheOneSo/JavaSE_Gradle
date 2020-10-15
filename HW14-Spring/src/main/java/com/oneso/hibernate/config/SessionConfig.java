package com.oneso.hibernate.config;

import com.oneso.hibernate.core.model.Address;
import com.oneso.hibernate.core.model.Phone;
import com.oneso.hibernate.core.model.User;
import com.oneso.hibernate.utils.HibernateUtils;
import org.hibernate.SessionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SessionConfig {

    @Bean
    public SessionFactory createSessionFactory() {
        return HibernateUtils.buildSessionFactory("hibernate.cfg.xml",
                User.class, Address.class, Phone.class);
    }
}
