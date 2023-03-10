package com.yanhuanxy.multifunexport.tools.domain.origin.dto;


public class DataTypeDTO {

    private Integer runNumber;
    private Integer totalNumber;
    private Integer errorNumber;
    private Integer initNumber;

    public DataTypeDTO() {
        super();
    }

    public Integer getRunNumber() {
        return runNumber;
    }

    public void setRunNumber(Integer runNumber) {
        this.runNumber = runNumber;
    }

    public Integer getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(Integer totalNumber) {
        this.totalNumber = totalNumber;
    }

    public Integer getErrorNumber() {
        return errorNumber;
    }

    public void setErrorNumber(Integer errorNumber) {
        this.errorNumber = errorNumber;
    }

    public Integer getInitNumber() {
        return initNumber;
    }

    public void setInitNumber(Integer initNumber) {
        this.initNumber = initNumber;
    }

    @Override
    public String toString() {
        return "DataTypeDTO{" +
                "runNumber=" + runNumber +
                ", totalNumber=" + totalNumber +
                ", errorNumber=" + errorNumber +
                ", initNumber=" + initNumber +
                '}';
    }
}
