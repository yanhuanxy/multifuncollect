package com.yanhuanxy.multifundomain.multcompertition.vo;

import java.util.List;

/**
 * socket 多人竞赛题目信息
 * @author yym
 */
public class CompetitionUserTopicsVO {

    private Long userId;

    private String userName;

    private List<Object> topicsGroup;

    private List<CompetitionAnswerVO> answerGroup;

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
}
