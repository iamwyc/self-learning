<html lang="zh">
<head>
  <meta charset="utf-8" />
  <title>Document</title>
  <style type="text/css">
    html, body { margin: 0; padding: 0; }
    body { color: #000; font-family:SimSun; font-size: 20px; }
    table { width: 100%; border-collapse: collapse; border-spacing: 0;padding: 0; margin: 0; }
    td, th { padding: 0; margin: 0; font-size: 100%; }

    @media print { @page { margin: 0; padding: 0; margin-bottom: 20px; } }
    .no-bold {font-weight:normal;}
    .breakline{word-wrap: break-word; word-break: normal; }
    .footer { margin-top: 10px; }
    @page {
      margin: 20mm 5mm 40mm 5mm;
    }

    #header {position: running(header);}
    #footer {position: running(footer);}
    @page{
      @top-center{
        content : element(header);
      }
      @bottom-center{
        content : element(footer)
      }
    }
    #pages:before{
      content : counter(page);
      font-size : 16px;
    }
    #pages:after{
      content : counter(pages);
      font-size : 16px;
    }
    #table{
      font-size : 18px;
      line-height:24px;
    }
    #content{
      font-size : 18px;
      line-height:24px;
    }
    #content2{
      font-size : 18px;
      line-height:24px;
      margin-top:20px;
    }
  </style>
</head>
<body>
<div>
  <div id="header" style="margin-top: 0px;">
    <span>页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉页眉</span>
  </div>
  <div id="footer">
    <div style="text-align: center;">第<span id="pages">页，共</span>页</div>
    <span style="margin-top:5px;">页底页底页底页底页底页底页底页底页底页底页底页底页底页底页底页底页底页底</span>
  </div>
  <br/>
  <div>
    <div id ="content"> <#if pageText??>${pageText}</#if> </div>
    <table class="table">
      <tr><td width="20%">编号x1</td><td width="30%">名称</td><td width="20%">编号2</td><td width="30%" align="center">编号3</td></tr>

                <#if tableList??>
                  <#list tableList as item >
                    <tr><td>${item.form}</td><td>${item.projectName}</td><td>${item.unitPrice}</td><td align="center">${item.orderAmount}</td></tr>
                  </#list>
                </#if>
    </table>
    <div id ="content2"> <#if pageText??>${pageText}</#if> </div>
  </div>

</div>
</body>
</html>