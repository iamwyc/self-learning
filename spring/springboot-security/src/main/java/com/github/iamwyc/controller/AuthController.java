package com.github.iamwyc.controller;

import com.github.iamwyc.config.BusinessException;
import com.github.iamwyc.config.ResultJson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@Slf4j
@RequestMapping("/api")
public class AuthController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @GetMapping("/auth")
  @ResponseBody
  public String auth(@RequestParam String username,@RequestParam String password) {
    log.info("username:{},password:{}", username, password);
    //用户验证
    Authentication authentication = authenticate(username, password);
    //存储认证信息
    SecurityContextHolder.getContext().setAuthentication(authentication);
    log.info("{} 登录成功");
    return "auth success";
  }

  private Authentication authenticate(String username, String password) {
    try {
      //该方法会去调用userDetailsService.loadUserByUsername()去验证用户名和密码，如果正确，则存储该用户名密码到“security 的 context中”
      return authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException | BadCredentialsException e) {
      throw new BusinessException(
          ResultJson.failure(ResultJson.SystemException.LOGIN_ERROR, e.getMessage()));
    }
  }
}
