package com.github.iamwyc.pdf;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/8/4 23:39
 */
public class Itext {

  // 生成PDF存放路径
  public static final String PDF_PATH = "D:\\tmp\\pdf";
  public static final String OUT_PDF_PATH = "D:\\tmp\\pdf\\demo.pdf";

  public static final String IMAGE_PATH = "D:\\tmp\\pdf\\1.jpg";

  public static final String MSYH_TTC = "D:\\tmp\\pdf\\msyh.ttc";

  public static void main(String[] args) throws Exception {
    pdf();
  }

  static {
    FontFactory.register(MSYH_TTC);
  }

  private static Font yahei12px = FontFactory.getFont("微软雅黑", BaseFont.IDENTITY_H, 12);

  public static void pdf() throws IOException, DocumentException {
    Rectangle rect = new Rectangle(PageSize.A4);
    Document document = new Document(rect, 10, 10, 10, 10);
    PdfWriter pdfWriter = PdfWriter.getInstance(document, new FileOutputStream(OUT_PDF_PATH));
    document.open();//打开PDF文件进行编辑
    Image image = Image.getInstance(IMAGE_PATH);

    document.newPage();
    document.add(image);
    Paragraph head1 = new Paragraph("团险保全明细", yahei12px);
    head1.setAlignment(1);//设置段落居中
    head1.setSpacingAfter(30);
    document.add(head1);
    document.add(paragraph("第二部分  协议条款", yahei12px, Paragraph.ALIGN_LEFT, 10.0f));
    document.add(paragraph("第二部分  协议条款", yahei12px, Paragraph.ALIGN_LEFT, 10.0f));
    document.add(paragraph("第二部分  协议条款", yahei12px, Paragraph.ALIGN_LEFT, 10.0f));
    document.add(paragraph("第二部分  协议条款", yahei12px, Paragraph.ALIGN_LEFT, 10.0f));
    document.add(paragraph(
        "    第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款第二部分协议条款",
        yahei12px, Paragraph.ALIGN_LEFT, 10.0f));

    document.newPage();
    createSimpleTable(document);
    document.close();
    pdfWriter.close();
  }

  private static Paragraph paragraph(String content, Font font) {
    return new Paragraph(content, font);
  }

  private static Paragraph paragraph(String content, Font font, int hAlign, float spacingBefore) {
    Paragraph paragraph = new Paragraph(content, font);
    paragraph.setAlignment(hAlign);
    paragraph.setSpacingBefore(spacingBefore);
    return paragraph;
  }

  private static String[] tableHead = {"序号", "名称", "日期", "金额"};

  public static void createSimpleTable(Document document) throws IOException, DocumentException {
    PdfPTable table = new PdfPTable(tableHead.length);
    table.setWidthPercentage(90f);
    for (int i = 0; i < tableHead.length; i++) {
      table.addCell(new Phrase(tableHead[i],yahei12px));
    }
    for (int i = 0; i < tableHead.length; i++) {
      // 构建每一格
      table.addCell(new Phrase(tableHead[i],yahei12px));
    }
    document.add(table);
  }
}
