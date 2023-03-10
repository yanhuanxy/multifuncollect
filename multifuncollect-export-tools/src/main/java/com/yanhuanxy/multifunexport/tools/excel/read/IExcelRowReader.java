package com.yanhuanxy.multifunexport.tools.excel.read;

import java.util.List;

/**
 * @author yym
 */
public interface IExcelRowReader {
    /**
     * @description 处理行数据
     * @param sheetIndex sheet页下标
     * @param curRow 当前行
     * @param rowlist 当前行数据
     */
    public void getRows(int sheetIndex, int curRow, List<String> rowlist);
}
