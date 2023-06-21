package com.yanhuanxy.multifundomain.multcompertition.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * socket 多人竞赛房间信息
 * @author yym
 */
public class CompetitionRoomInfoVO {

    private String roomNo;

    private List<Object> topicsGroup;

    private Map<Long , CompetitionUserInfoVO> roomUsers;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeRoomStartTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    private LocalDateTime activeRoomEndTime;

    private Integer competitionStatus;

    private Long intervalTimeSeconds;

    public CompetitionRoomInfoVO(Integer roomUserNum) {
        this.roomUsers = new ConcurrentHashMap<>(roomUserNum);
        this.competitionStatus = 1;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public List<Object> getTopicsGroup() {
        return topicsGroup;
    }

    public void setTopicsGroup(List<Object> topicsGroup) {
        this.topicsGroup = topicsGroup;
    }

    public Map<Long, CompetitionUserInfoVO> getRoomUsers() {
        return roomUsers;
    }

    public void setRoomUsers(Map<Long, CompetitionUserInfoVO> roomUsers) {
        this.roomUsers = roomUsers;
    }

    public Integer getCompetitionStatus() {
        return competitionStatus;
    }

    public void setCompetitionStatus(Integer competitionStatus) {
        this.competitionStatus = competitionStatus;
    }

    public Long getIntervalTimeSeconds() {
        return intervalTimeSeconds;
    }

    public void setIntervalTimeSeconds(Long intervalTimeSeconds) {
        this.intervalTimeSeconds = intervalTimeSeconds;
    }

    public LocalDateTime getActiveRoomStartTime() {
        return activeRoomStartTime;
    }

    public void setActiveRoomStartTime(LocalDateTime activeRoomStartTime) {
        this.activeRoomStartTime = activeRoomStartTime;
    }

    public LocalDateTime getActiveRoomEndTime() {
        return activeRoomEndTime;
    }

    public void setActiveRoomEndTime(LocalDateTime activeRoomEndTime) {
        this.activeRoomEndTime = activeRoomEndTime;
    }
}
