package com.github.iamwyc.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/8/10 21:51
 */
@Data
@AllArgsConstructor
public class ResultJson {

  public enum SystemException {
    UNAUTHORIZED, FORBIDDEN, LOGIN_ERROR;
  }

  private String msg;
  private Integer code;

  public static ResultJson failure(SystemException e, String msg) {

    return new ResultJson(msg, e.ordinal());
  }
}
