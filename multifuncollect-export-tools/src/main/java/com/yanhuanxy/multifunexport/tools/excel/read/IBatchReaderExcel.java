package com.yanhuanxy.multifunexport.tools.excel.read;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 *  * poi 事件模式读取excel
 *  * @author yym
 *  * @date 20220831 18:07
 */
public interface IBatchReaderExcel  {

    /**
     * 行数据处理器
     * @param rowReader rowReader
     */
    public void setRowReader(IExcelRowReader rowReader);
    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    public void process(String fileName) throws IOException;

    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    public void process(File file) throws IOException;

    /**
     * 遍历excel下所有的sheet
     *
     * @throws IOException
     */
    public void process(InputStream inputStream) throws IOException;

}
