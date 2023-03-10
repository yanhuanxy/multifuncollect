package com.yanhuanxy.multifunexport.tools.excel;

import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;

public class CsvUploadUtil {
    private static final Logger logger = LoggerFactory.getLogger(CsvUploadUtil.class);

    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件行分隔符
     */
    private static final String CSV_ROW_SEPARATOR = System.lineSeparator();

    //CSV文件分隔符
    private final static String NEW_LINE_SEPARATOR="\n";

    /** CSV文件列分隔符 */
    private static final String CSV_RN = "\r\n";


    /**
     * @param response 响应流
     * @param fileName 文件名称
     * @param titleColumn 标题列名称对象（如：name）
     * @param titleName 标题列名称对象描述（如：张三）
     * @param dataList  数据源
     */
    public static void writeCSV(HttpServletResponse response, String fileName, String titleColumn[], String titleName[], List<?> dataList) {
      OutputStream out = null;
      try {
        // 保证线程安全
        StringBuffer buf = new StringBuffer();
        out = response.getOutputStream();
        String lastFileName = fileName + ".csv";
        response.setContentType("application/msexcel;charset=UTF-8");
        response.setHeader("Content-Disposition", "attachment; filename="+ URLEncoder.encode(lastFileName, "UTF-8"));

        // 组装表头
        for (String title : titleName) {
          buf.append(title).append(CSV_COLUMN_SEPARATOR);
        }
        buf.append(CSV_ROW_SEPARATOR);

        //组装行数据
        for (int index = 0; index < dataList.size(); index++) {
          Object obj = dataList.get(index);
          Class clazz = obj.getClass();
          for (int columnIndex = 0; columnIndex < titleColumn.length; columnIndex++) {
            String title = titleColumn[columnIndex].trim();
            if (!"".equals(title)) {
              // 获取返回类型
              String UTitle = Character.toUpperCase(title.charAt(0)) + title.substring(1, title.length()); // 使其首字母大写;
              String methodName = "get" + UTitle;
              Method method = clazz.getDeclaredMethod(methodName);
              String returnType = method.getReturnType().getName();
              Object object = method.invoke(obj);
              //获取到数据
              String data = method.invoke(obj) == null ? "" : object.toString();
              //组装数据
              buf.append(data).append(CSV_COLUMN_SEPARATOR);
            }
          }
          buf.append(CSV_ROW_SEPARATOR);
        }
        //输出
        out.write(buf.toString().getBytes("UTF-8"));
      } catch (Exception e) {
        logger.error("CSV 写出异常！{}", e.getMessage());
      } finally {
        if (out != null) {
          try {
            out.flush();
            out.close();
          } catch (IOException e) {
            logger.error(e.getMessage());
          }
        }
      }
    }


  public static ByteArrayOutputStream doExport(String[] colNames, String[] mapKeys, List<Map> dataList) {
    try {
      StringBuffer buf = new StringBuffer();

      // 完成数据csv文件的封装
      // 输出列头
      for (int i = 0; i < colNames.length; i++) {
        buf.append(colNames[i]).append(CSV_COLUMN_SEPARATOR);
      }
      buf.append(CSV_RN);

      if (null != dataList) { // 输出数据
        for (int i = 0; i < dataList.size(); i++) {
          for (int j = 0; j < mapKeys.length; j++) {
            buf.append(dataList.get(i).get(mapKeys[j])).append(CSV_COLUMN_SEPARATOR);
          }
          buf.append(CSV_RN);
        }
      }
      // 写出响应
      ByteArrayOutputStream os = new ByteArrayOutputStream();
      os.write(buf.toString().getBytes("GBK"));
      os.flush();
      os.close();
      return os;
    } catch (Exception e) {
      logger.error("CSV 写出异常！{}", e.getMessage());
    }
    return null;
  }


  /**
   * 仅仅写出数据到 csv
   * @param filePath 文件路劲
   * @param dataList 数据
   */
  public static void writerToCsv(String filePath, List<List<String>> dataList){
    try (FileOutputStream fileOutputStream = new FileOutputStream(new File(filePath))){
      if (null != dataList) {
        OutputStreamWriter writer = new OutputStreamWriter(fileOutputStream, "GBK");
        BufferedWriter bufferedWriter = new BufferedWriter(writer);
        // 输出数据
        for (List<String> dataItem : dataList){
          StringBuffer buf = new StringBuffer();
          for (String tmpdata : dataItem) {
            buf.append(tmpdata).append(CSV_COLUMN_SEPARATOR);
          }
          buf.append(CSV_RN);
          bufferedWriter.write(buf.toString());
        }
        bufferedWriter.flush();
        bufferedWriter.close();
        writer.close();
      }
    } catch (Exception e) {
      logger.error("CSV 写出异常！{}", e.getMessage());
    }
  }
}

