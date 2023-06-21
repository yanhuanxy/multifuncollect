package com.yanhuanxy.multifundomain.multcompertition.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * socket 多人竞赛用户信息
 * @author yym
 */
public class CompetitionUserInfoVO {

    private Long userId;

    private String userName;

    private List<Object> topicsGroup;

    private List<CompetitionAnswerVO> answerGroup;

    private Long activeTopicId;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeTopicStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeTopicEndTime;

    private Integer score;

    public CompetitionUserInfoVO(Long userId, String userName) {
        this.userId = userId;
        this.userName = userName;
        this.score = 0;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<Object> getTopicsGroup() {
        return topicsGroup;
    }

    public void setTopicsGroup(List<Object> topicsGroup) {
        this.topicsGroup = topicsGroup;
    }

    public List<CompetitionAnswerVO> getAnswerGroup() {
        return answerGroup;
    }

    public void setAnswerGroup(List<CompetitionAnswerVO> answerGroup) {
        this.answerGroup = answerGroup;
    }

    public Long getActiveTopicId() {
        return activeTopicId;
    }

    public void setActiveTopicId(Long activeTopicId) {
        this.activeTopicId = activeTopicId;
    }

    public LocalDateTime getActiveTopicStartTime() {
        return activeTopicStartTime;
    }

    public void setActiveTopicStartTime(LocalDateTime activeTopicStartTime) {
        this.activeTopicStartTime = activeTopicStartTime;
    }

    public LocalDateTime getActiveTopicEndTime() {
        return activeTopicEndTime;
    }

    public void setActiveTopicEndTime(LocalDateTime activeTopicEndTime) {
        this.activeTopicEndTime = activeTopicEndTime;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
