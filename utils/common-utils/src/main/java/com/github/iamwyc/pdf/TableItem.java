package com.github.iamwyc.pdf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/8/6 21:48
 */
@Data
@AllArgsConstructor
public class TableItem {

  public static List<TableItem> tableList;

  static {
    Random random = new Random();
    tableList = new ArrayList<>();
    for (int i = 0; i < 5; i++) {
      tableList
          .add(new TableItem(UUID.randomUUID().toString(), System.currentTimeMillis() + "",
              random.nextInt() + "",random.nextInt() + ""));
    }
  }

  private String projectName;
  private String form;
  private String unitPrice;
  private String orderAmount;
}
