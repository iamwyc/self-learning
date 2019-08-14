package com.github.iamwyc.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/8/10 22:10
 */
@RestControllerAdvice
@Slf4j
public class DefaultExceptionHandler {

  @ExceptionHandler(BusinessException.class)
  public ResultJson handleCustomException(BusinessException e) {
    log.error("BusinessException:{}",e);
    return e.getResultJson();
  }
}
