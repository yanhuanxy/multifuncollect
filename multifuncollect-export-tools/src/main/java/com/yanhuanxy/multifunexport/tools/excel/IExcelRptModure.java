package com.yanhuanxy.multifunexport.tools.excel;

import java.util.Map;

/**
 * @author yym
 */
public interface IExcelRptModure {
    /**
     * 修改sheet
     */
    Map<String, Map<String, Object>> updateSheet(String sheetIndex);
}
