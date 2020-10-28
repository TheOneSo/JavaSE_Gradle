package com.oneso.hibernate.config;

import com.oneso.hibernate.cache.HwCache;
import com.oneso.hibernate.cache.MyCache;
import com.oneso.hibernate.core.model.Address;
import com.oneso.hibernate.core.model.Phone;
import com.oneso.hibernate.core.model.User;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheConfig {

  @Bean
  @Qualifier("userCache")
  public HwCache<Long, User> createUserCache() {
    return new MyCache<>();
  }

  @Bean
  @Qualifier("addressCache")
  public HwCache<Long, Address> createAddressCache() {
    return new MyCache<>();
  }

  @Bean
  @Qualifier("phoneCache")
  public HwCache<Long, Phone> createPhoneCache() {
    return new MyCache<>();
  }
}
