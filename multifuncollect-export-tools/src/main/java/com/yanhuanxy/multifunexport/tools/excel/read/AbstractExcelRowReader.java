package com.yanhuanxy.multifunexport.tools.excel.read;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * poi事件模式读取行
 * @author yym
 * @date 20210415 22:07
 */
public abstract class AbstractExcelRowReader implements IExcelRowReader {
    private static final Logger logger = LoggerFactory.getLogger(AbstractExcelRowReader.class);
    /**
     *
     * @param sheetIndex sheet页下标
     * @param curRow 当前行
     * @param rowlist 当前行数据
     */
    @Override
    public void getRows(int sheetIndex, int curRow, List<String> rowlist) {
        logger.info("第"+ curRow +"行");
        for (int i = 0; i < rowlist.size(); i++) {
            System.out.print("".equals(rowlist.get(i))?"*":rowlist.get(i) + " ");
        }
    }
}
