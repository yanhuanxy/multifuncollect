package com.yanhuanxy.multifundomain.multcompertition.vo;

/**
 * socket 多人竞赛用户得分信息
 * @author yym
 */
public class CompetitionUserScoreDetailVO {

    private Long userId;

    private String userName;

    private Integer totalTopic;

    private Integer rightTopic;

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

    public Integer getTotalTopic() {
        return totalTopic;
    }

    public void setTotalTopic(Integer totalTopic) {
        this.totalTopic = totalTopic;
    }

    public Integer getRightTopic() {
        return rightTopic;
    }

    public void setRightTopic(Integer rightTopic) {
        this.rightTopic = rightTopic;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }
}
