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
import com.lowagie.text.pdf.PdfArray;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfDictionary;
import com.lowagie.text.pdf.PdfName;
import com.lowagie.text.pdf.PdfObject;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
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
  public static final String OUT_PDF_PATH = "D:\\tmp\\pdf\\demo_out.pdf";

  public static final String DEMO_PDF_PATH = "D:\\tmp\\pdf\\demo.pdf";

  public static final String IMAGE_PATH = "D:\\tmp\\pdf\\1.jpg";

  public static final String MSYH_TTC = "D:\\tmp\\pdf\\msyh.ttc";

  public static final String MSYH_TTF = "D:\\tmp\\pdf\\msyh.ttf";

  static {
    FontFactory.register(MSYH_TTF);
  }

  private static Font yahei12px = FontFactory.getFont("微软雅黑", BaseFont.IDENTITY_H, 12);
  private static Font msyhTTF;

  static {
    try {
      msyhTTF = new Font(
          BaseFont.createFont(MSYH_TTF, BaseFont.IDENTITY_H, BaseFont.EMBEDDED));
    } catch (DocumentException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static void main(String[] args) throws Exception {
    editPDF();
  }

  public static void editPDF() throws IOException, DocumentException {
    //创建一个pdf读入流
    PdfReader reader = new PdfReader(DEMO_PDF_PATH);
    //根据一个pdfreader创建一个pdfStamper.用来生成新的pdf.
    PdfStamper stamper = new PdfStamper(reader,
        new FileOutputStream(OUT_PDF_PATH));

    for (int i = 1; i <= reader.getNumberOfPages(); i++) {
      //获得pdfstamper在当前页的上层打印内容.也就是说这些内容会覆盖在原先的pdf内容之上.
      PdfContentByte over = stamper.getOverContent(i);
      //用pdfreader获得当前页字典对象.包含了该页的一些数据.比如该页的坐标轴信息.
      PdfDictionary p = reader.getPageN(i);
      //拿到mediaBox里面放着该页pdf的大小信息.
      PdfObject po = p.get(new PdfName("MediaBox"));
      System.out.println(po.isArray());
      //po是一个数组对象.里面包含了该页pdf的坐标轴范围.
      PdfArray pa = (PdfArray) po;
      System.out.println(pa.size());
      //看看y轴的最大值.
      System.out.println(pa.getAsNumber(pa.size() - 1));
      //开始写入文本
      over.beginText();
      //设置字体和大小
      over.setFontAndSize(msyhTTF.getBaseFont(), 10);
      //设置字体的输出位置
      over.setTextMatrix(107, 540);
      //要输出的text
      over.showText("我要加[终稿]字样" + i);
      over.endText();
      //创建一个image对象.
      Image image = Image.getInstance(IMAGE_PATH);
      //设置image对象的输出位置pa.getAsNumber(pa.size()-1).floatValue()是该页pdf坐标轴的y轴的最大值
      image.setAbsolutePosition(0,
          pa.getAsNumber(pa.size() - 1).floatValue() - 100);//0,0,841.92,595.32
      over.addImage(image);

      //画一个圈.
      over.setRGBColorStroke(0xFF, 0x00, 0x00);
      over.setLineWidth(5f);
      over.ellipse(250, 450, 350, 550);
      over.stroke();
    }
    stamper.close();
  }

  public static void createpdf() throws IOException, DocumentException {
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
      table.addCell(new Phrase(tableHead[i], msyhTTF));
    }
    for (int i = 0; i < tableHead.length; i++) {
      // 构建每一格
      table.addCell(new Phrase(tableHead[i], msyhTTF));
    }
    document.add(table);
  }
}
