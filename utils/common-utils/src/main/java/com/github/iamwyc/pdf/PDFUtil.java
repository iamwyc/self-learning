package com.github.iamwyc.pdf;

import com.lowagie.text.DocumentException;
import com.lowagie.text.pdf.BaseFont;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.ResourceUtils;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

/**
 * @author : iamwyc
 * @version : 1.0
 * @date : 2019/8/6 21:37
 */
public class PDFUtil {

  public static String create() throws Exception {
    Map<String, Object> data = new HashMap<>(2);
    String text =
        "&#160;&#160;根据协议规定：2019年乙方保底投放额为¥10000000，年度保底金额分摊至每个季度进行结算,季度结算时如实际金额超过保底金额则按实际结算,"
            + "如低于保底金额，则乙方按保底金额结算至甲方。第二季度保底额为¥10000000<br/>&#160;&#160;"
            + "2019年1月，乙方结算给甲方金额为￥1,577,260 (大写人民币壹佰伍拾柒万柒仟贰佰陆拾元整)，2019年2月，"
            + "乙方应结算给甲方金额为￥1,143,720(大写人民币壹佰壹拾肆万叁仟柒佰贰拾元整)。根据表格，2019年3月，"
            + "乙方应结算给甲方金额为￥3,313,138 (大写人民币叁佰叁拾壹万叁仟壹佰叁拾捌元整)。因此,第二季度乙方共结"
            + "算￥6,034,118，即完成第二季度保底金额。" + "<br/>&#160;&#160;甲方向乙方提供相应金额的的增值税发票。<br/><br/>";
    data.put("tableList", TableItem.tableList);
    data.put("pageText", text);
    return createPDFbyFtl("pdf.ftl", data);
  }

  public static String createPDFbyFtl(String ftlName, Map<String, Object> data)
      throws IOException, TemplateException, DocumentException {

    SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-dd");

    String name = String
        .format("out_%s%03d.pdf", format.format(new Date()), new Random().nextInt());

    String render = freeMarkerRender(data, ftlName);
    System.out.println(render);
    File path = new File(ResourceUtils.getURL("classpath:").getPath());
    File upload = new File(path.getAbsolutePath(), "static/images/upload/");
    if (!upload.exists()) {
      upload.mkdirs();
    }
    System.out.println(path);
    FileOutputStream fos = new FileOutputStream(upload + "\\" + name);
    createPdf(render, fos);
    return name;
  }

  static Configuration freemarkerCfg;

  static {
    freemarkerCfg = new Configuration(Configuration.VERSION_2_3_21);
    //freemarker的模板目录
    try {

      ClassPathResource resource = new ClassPathResource("ftl");
      freemarkerCfg.setDirectoryForTemplateLoading(resource.getFile());
      freemarkerCfg.setEncoding(Locale.CHINA, "UTF-8");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static String freeMarkerRender(Map<String, Object> data, String htmlTmp)
      throws IOException, TemplateException {
    Writer out = new StringWriter();
    // 获取模板,并设置编码方式
    Template template = freemarkerCfg.getTemplate(htmlTmp);
    // 合并数据模型与模板
    template.process(data, out);
    out.flush();
    return out.toString();
  }

  private static void createPdf(String content, OutputStream out)
      throws IOException, com.lowagie.text.DocumentException {
    ITextRenderer render = new ITextRenderer();

    ITextFontResolver fontResolver = render.getFontResolver();
    ClassPathResource resource = new ClassPathResource("msyh.ttc");
    fontResolver.addFont(resource.getPath(), BaseFont.IDENTITY_H,
        BaseFont.NOT_EMBEDDED);

    // 解析html生成pdf
    render.setDocumentFromString(content);
    render.layout();
    render.createPDF(out);
    render.finishPDF();
  }
}
