package com.yanhuanxy.multifunservice.excelupload.listen;

import com.yanhuanxy.multifuncommon.enums.ReadEventTypeEnum;
import com.yanhuanxy.multifundomain.excelupload.dto.UploadDataExcelDTO;

public class ReaderDataEvent {

    private UploadDataExcelDTO beforeUploadDataExcelConf;

    private ReadEventTypeEnum eventTypeEnum;

    public UploadDataExcelDTO getBeforeUploadDataExcelConf() {
        return beforeUploadDataExcelConf;
    }

    public void setBeforeUploadDataExcelConf(UploadDataExcelDTO beforeUploadDataExcelConf) {
        this.beforeUploadDataExcelConf = beforeUploadDataExcelConf;
    }

    public ReadEventTypeEnum getEventTypeEnum() {
        return eventTypeEnum;
    }

    public void setEventTypeEnum(ReadEventTypeEnum eventTypeEnum) {
        this.eventTypeEnum = eventTypeEnum;
    }
}
