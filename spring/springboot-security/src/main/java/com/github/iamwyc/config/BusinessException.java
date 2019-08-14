package com.github.iamwyc.config;

import lombok.Data;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/8/10 22:09
 */
@Data
public class BusinessException extends RuntimeException {

  private ResultJson resultJson;

  public BusinessException(ResultJson resultJson) {
    this.resultJson = resultJson;
  }

}
