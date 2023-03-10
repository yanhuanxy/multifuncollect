package com.yanhuanxy.multifunexport.tools.domain.emuns.excel;

/**
 * 单元格中的数据可能的数据类型
 * @author yym
 * @date 20190916 18:07
 */
public enum CellDataEnums {
    // 单元格类型
    BOOL, ERROR, FORMULA,
    INLINESTR, SSTINDEX, NUMBER,
    DATE, NULL;
}
