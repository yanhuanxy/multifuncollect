package com.yanhuanxy.multifunservice.excelupload.listen;

public interface ReaderListener {

    /**
     * 写入数据
     */
    void insertData(ReaderDataEvent readerDataEvent);
}
