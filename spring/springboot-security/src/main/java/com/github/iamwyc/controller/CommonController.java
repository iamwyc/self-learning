package com.github.iamwyc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/8/10 21:34
 */
@Controller
@Slf4j
@RequestMapping("log")
public class CommonController {
  @GetMapping
  @ResponseBody
  public String logDemo() throws Exception {

    log.trace("trace查询了学生信息");

    return "logDemo";
  }
}
