package com.yanhuanxy.multifunservice.excelupload.listen;

/**
 * 监听源
 */
public interface ReaderListenerAble {

    /**
     * 注册监听
     * @param readerDataListener 监听器
     */
    void registerListener(ReaderListener readerDataListener);


    /**
     * 触发监听
     * @param readerDataEvent 事件
     */
    void triggerReadListener(ReaderDataEvent readerDataEvent);
}
