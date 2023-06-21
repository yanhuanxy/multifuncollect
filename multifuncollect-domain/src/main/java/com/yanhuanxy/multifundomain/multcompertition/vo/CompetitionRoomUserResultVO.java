package com.yanhuanxy.multifundomain.multcompertition.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

/**
 * socket 多人竞赛房间内用户正在作答信息
 * @author yym
 */
public class CompetitionRoomUserResultVO {

    /*@Schema(description = "房间号")*/
    private String roomNo;

    /*@Schema(description = "用户id")*/
    private Long userId;

    /*@Schema(description = "用户名称")*/
    private String userName;

    /*@Schema(description = "得分")*/
    private Integer score;

    /*@Schema(description = "题目总分")*/
    private Integer totalScore;

    /*@Schema(description = "用户试题组")*/
    private List<Object> topicsGroup;

    /*@Schema(description = "房间用户分数排名")*/
    private List<CompetitionUserScoreVO> userScoreRankInfo;

    /*@Schema(description = "正在作答的题目id")*/
    private Long activeTopicId;

    /*@Schema(description = "正在作答题目结束时间（时间格式：yyyy-MM-dd HH:mm:ss）")*/
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeTopicEndTime;

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
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

    public Integer getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(Integer totalScore) {
        this.totalScore = totalScore;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public List<CompetitionUserScoreVO> getUserScoreRankInfo() {
        return userScoreRankInfo;
    }

    public void setUserScoreRankInfo(List<CompetitionUserScoreVO> userScoreRankInfo) {
        this.userScoreRankInfo = userScoreRankInfo;
    }

    public Long getActiveTopicId() {
        return activeTopicId;
    }

    public void setActiveTopicId(Long activeTopicId) {
        this.activeTopicId = activeTopicId;
    }

    public LocalDateTime getActiveTopicEndTime() {
        return activeTopicEndTime;
    }

    public void setActiveTopicEndTime(LocalDateTime activeTopicEndTime) {
        this.activeTopicEndTime = activeTopicEndTime;
    }
}
