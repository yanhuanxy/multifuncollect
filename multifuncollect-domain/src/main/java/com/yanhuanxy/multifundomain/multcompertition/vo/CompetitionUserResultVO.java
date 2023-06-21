package com.yanhuanxy.multifundomain.multcompertition.vo;

import java.util.List;

/**
 * socket 多人竞赛房间内用户竞赛结果信息
 * @author yym
 */
public class CompetitionUserResultVO {

    private Integer totalIntegral;

    private Integer integral;

    private String userName;

    private List<CompetitionUserScoreDetailVO> userScoreRankInfo;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public List<CompetitionUserScoreDetailVO> getUserScoreRankInfo() {
        return userScoreRankInfo;
    }

    public void setUserScoreRankInfo(List<CompetitionUserScoreDetailVO> userScoreRankInfo) {
        this.userScoreRankInfo = userScoreRankInfo;
    }

    public Integer getTotalIntegral() {
        return totalIntegral;
    }

    public void setTotalIntegral(Integer totalIntegral) {
        this.totalIntegral = totalIntegral;
    }

    public Integer getIntegral() {
        return integral;
    }

    public void setIntegral(Integer integral) {
        this.integral = integral;
    }

}
