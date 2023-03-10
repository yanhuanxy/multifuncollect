package com.yanhuanxy.multifunexport.tools.excel.read;

import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 *  * 分段读取excel csv
 *  * @author yym
 *  * @date 20220413 18:07
 */
public class BatchReaderExcelcsv implements IBatchReaderExcel {
    private static final Logger logger = LoggerFactory.getLogger(BatchReaderExcelcsv.class);

    /** 文件编码 **/
    private String charsetName = "gbk";

    /** 分隔符 **/
    private String delimiter = ",";

    /** 最大列 **/
    private int maxColumns = -1;

    /** 表索引 */
    private int sheetIndex = 0;

    /** 当前行 */
    private int curRow = 0;

    /** 存储行记录的容器 */
    private List<String> rowlist = new ArrayList<String>();;

    /**
     *匹配引号内的数据
     */
    private static Pattern cellValCompile = Pattern.compile("(?<=^\").*(?=\"$)");

    private IExcelRowReader rowReader;

    public BatchReaderExcelcsv() {
        super();
    }

    public BatchReaderExcelcsv(File file) throws IOException {
        this.setFileCharsetName(file);
    }

    public BatchReaderExcelcsv(InputStream inputStream) throws IOException{
        this.setFileCharsetName(inputStream);
    }

    @Override
    public void setRowReader(IExcelRowReader rowReader) {
        this.rowReader = rowReader;
    }

    public void setCharsetName(String charsetName) {
        this.charsetName = charsetName;
    }

    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    @Override
    public void process(String fileName) throws IOException {
        try(FileInputStream fileInputStream= new FileInputStream(new File(fileName))){
            process(fileInputStream);
        }
    }

    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    @Override
    public void process(File file) throws IOException {
        try(FileInputStream fileInputStream= new FileInputStream(file)){
            process(fileInputStream);
        }
    }

    public void setFileCharsetName(String fileName) throws IOException{
        this.setFileCharsetName(new File(fileName));
    }

    public void setFileCharsetName(File file) throws IOException{
        try(FileInputStream  inputStream = new FileInputStream(file)) {
            this.setFileCharsetName(inputStream);
        }
    }

    /**
     * 获取文件编码
     * 注意：一般情况下可以用下 但是可能不准
     * 需要准确的格式引入文件格式插件包
     * @param inputStream
     * @return String
     * @throws Exception
     * @author yym
     * @date 20220824 10:55
     */
    public void setFileCharsetName(InputStream inputStream) throws IOException {
        try(BufferedInputStream bin = new BufferedInputStream(inputStream)) {
            String code = null;
            int p = (bin.read() << 8) + bin.read();
            switch (p) {
                case 0xefbb:
                    code = "UTF-8";
                    break;
                case 0xfffe:
                    code = "Unicode";
                    break;
                case 0xfeff:
                    code = "UTF-16BE";
                    break;
                default:
                    code = "GBK";
            }
            this.setCharsetName(code);
        }
    }

    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    public void process(InputStream inputStream) throws IOException {
        try(BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, charsetName))){
            String line;
            while ((line = bufferedReader.readLine()) != null){
                if(ObjectUtils.isEmpty(line) || ObjectUtils.isEmpty(line.replaceAll(delimiter,""))){
                    curRow++;
                    continue;
                }
                // ""xxx,","xss","xxs""
                String[] lineVals = line.split(delimiter);
                if(curRow == 0){
                    maxColumns = lineVals.length;
                }
                List<String> lineList = Arrays.stream(lineVals).map(tmpVal-> {
                    if(ObjectUtils.isNotEmpty(tmpVal)){
                        Matcher matcher = cellValCompile.matcher(tmpVal);
                        if(matcher.find()){
                            return matcher.group();
                        }
                    }
                    return tmpVal;
                }).collect(Collectors.toList());
                rowlist.addAll(lineList);
                if(maxColumns > rowlist.size()){
                    addCellNullVal(rowlist.size(), maxColumns);
                }
                // 每行结束时， 调用getRows() 方法
                rowReader.getRows(sheetIndex, curRow, rowlist);
                // 清空容器
                rowlist.clear();
                curRow++;
            }
        }
    }

    /**
     * // 补全空的单元格
     */
    private void addCellNullVal(int curColumn, int maxColumn){
        int len = maxColumn - curColumn;
        for (int i = 0; i < len; i++) {
            rowlist.add("");
        }
    }

    /**
     * 校验空字符串时 返回 " "空格
     * @param tmpVal 值
     * @return string
     */
    private String checkValIsNull(String tmpVal){
        if("".equals(tmpVal)){
            return " ";
        }
        return tmpVal;
    }
}
