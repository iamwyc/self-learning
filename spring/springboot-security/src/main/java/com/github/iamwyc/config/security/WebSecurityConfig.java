package com.github.iamwyc.config.security;

import com.github.iamwyc.config.ResultJson;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.formLogin()
        .loginPage("/index.html")
        .and()
        .authorizeRequests()
        .antMatchers("/index.html", "/api/auth").permitAll()// 定义哪些URL需要被保护、哪些不需要被保护
        .anyRequest() // 任何请求,登录后可以访问
        .authenticated()
        .and()
        .csrf().disable()
        .exceptionHandling().authenticationEntryPoint(customerAuthenticationEntryPoint())
        .accessDeniedHandler(customerAccessDeniedHandler());
  }

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().antMatchers("/v2/api-docs",
        "/swagger-resources/configuration/ui",
        "/swagger-resources",
        "/swagger-resources/configuration/security",
        "/swagger-ui.html"
    );
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }


  /**
   * 自定义权限不足的处理类
   */
  public AccessDeniedHandler customerAccessDeniedHandler() {

    return new AccessDeniedHandler() {

      @Override
      public void handle(HttpServletRequest httpServletRequest,
          HttpServletResponse response, AccessDeniedException e)
          throws IOException, ServletException {
        log.error("****** 没有权限 *****");
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        response.setStatus(200);
        String body = ResultJson.failure(ResultJson.SystemException.FORBIDDEN, "没有权限")
            .toString();
        printWriter.write(body);
        printWriter.flush();
      }
    };
  }

  /**
   * 自定义没有认证的处理类
   */
  public AuthenticationEntryPoint customerAuthenticationEntryPoint() {

    return new AuthenticationEntryPoint() {
      @Override
      public void commence(HttpServletRequest httpServletRequest,
          HttpServletResponse response, AuthenticationException e)
          throws IOException, ServletException {
        log.error("****** 没有进行身份认证 *****");
        response.setStatus(200);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter printWriter = response.getWriter();
        String body = ResultJson.failure(ResultJson.SystemException.UNAUTHORIZED, "没有登录")
            .toString();
        printWriter.write(body);
        printWriter.flush();
      }
    };
  }
}
