package com.github.iamwyc.config.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/8/10 21:39
 */
@Slf4j
@Component
public class SecurityService implements UserDetailsService {

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("用户的用户名: {}", username);
    // TODO 根据用户名，查找到对应的密码，与权限
    String password = passwordEncoder.encode("123456");
    // 封装用户信息，并返回。参数分别是：用户名，密码，用户权限
    User user = new User(username, password,
        AuthorityUtils.commaSeparatedStringToAuthorityList("admin"));
    return user;
  }
}
