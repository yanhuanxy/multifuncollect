package com.yanhuanxy.multifundomain.multcompertition.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * socket 多人竞赛信息
 * @author yym
 */
public class CompetitionDoAnswerParam {

    /*@Parameter(description = "房间号")*/
    private String roomNo;

    /*@Parameter(description = "题目ID")
    @NotNull(message = "题目类型不能为空！")*/
    private Long topicId;

    /*@Parameter(description = "下一题题目ID")
    @NotNull(message = "题目类型不能为空！")*/
    private Long nextTopicIdId;

    /*@Parameter(description = "题目类型<0：单选题；1：多选题；2：判断题>", required = true)
    @NotNull(message = "题目类型不能为空！")*/
    private Integer type;

    /*@Parameter(description = "答案内容")
    @NotNull(message = "答案内容不能为空！")*/
    private String answer;

    /*@Parameter(description = "题目作答时间（时间格式：yyyy-MM-dd HH:mm:ss）")*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeAnswerTime;


    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public Long getNextTopicIdId() {
        return nextTopicIdId;
    }

    public void setNextTopicIdId(Long nextTopicIdId) {
        this.nextTopicIdId = nextTopicIdId;
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

    public LocalDateTime getActiveAnswerTime() {
        return activeAnswerTime;
    }

    public void setActiveAnswerTime(LocalDateTime activeAnswerTime) {
        this.activeAnswerTime = activeAnswerTime;
    }

}
