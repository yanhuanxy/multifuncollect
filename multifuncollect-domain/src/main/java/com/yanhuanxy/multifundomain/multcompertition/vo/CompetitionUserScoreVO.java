package com.yanhuanxy.multifundomain.multcompertition.vo;

/**
 * socket 多人竞赛用户得分信息
 * @author yym
 */
public class CompetitionUserScoreVO {

    private Long userId;

    private String userName;

    private Integer score;

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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
