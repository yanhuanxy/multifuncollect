package com.yanhuanxy.multifundomain.multcompertition.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * socket 多人竞赛用户题目及答案信息
 * @author yym
 */
public class CompetitionAnswerVO {

    /*@Schema(description = "题目ID")*/
    private Long id;

    /*@Schema(description = "题目类型<0：单选题；1：多选题；2：判断题>", requiredMode = Schema.RequiredMode.REQUIRED)*/
    private Integer type;

    /*@Schema(description = "答案", requiredMode = Schema.RequiredMode.REQUIRED)*/
    private String answer;

    /*@Schema(description = "结果")*/
    private boolean result;

    /*@Schema(description = "题目开始时间（时间格式：yyyy-MM-dd HH:mm:ss）")*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeAnswerStartTime;

    /*@Schema(description = "题目作答时间（时间格式：yyyy-MM-dd HH:mm:ss）")*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeAnswerTime;

    /*@Schema(description = "题目结束时间（时间格式：yyyy-MM-dd HH:mm:ss）")*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeAnswerEndTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public LocalDateTime getActiveAnswerStartTime() {
        return activeAnswerStartTime;
    }

    public void setActiveAnswerStartTime(LocalDateTime activeAnswerStartTime) {
        this.activeAnswerStartTime = activeAnswerStartTime;
    }

    public LocalDateTime getActiveAnswerTime() {
        return activeAnswerTime;
    }

    public void setActiveAnswerTime(LocalDateTime activeAnswerTime) {
        this.activeAnswerTime = activeAnswerTime;
    }

    public LocalDateTime getActiveAnswerEndTime() {
        return activeAnswerEndTime;
    }

    public void setActiveAnswerEndTime(LocalDateTime activeAnswerEndTime) {
        this.activeAnswerEndTime = activeAnswerEndTime;
    }
}
