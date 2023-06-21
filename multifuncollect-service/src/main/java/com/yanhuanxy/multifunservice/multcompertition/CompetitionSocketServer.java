package com.yanhuanxy.multifunservice.multcompertition;

import com.yanhuanxy.multifuncommon.base.vo.ResponseVO;
import com.yanhuanxy.multifundomain.multcompertition.dto.CompetitionDoAnswerParam;

public interface CompetitionSocketServer {

    /**
     * 选择在线用户 分配房间
     * 1、分配房间 初始化房间数据 抽取试题
     * 2、重连 重新进入房间 获取旧数据
     * 返回 当前房间题组 + 排行榜
     * @param userId 用户id
     */
    ResponseVO<Object> chooseUserToCompetition(Long userId);


    /**
     * 重新进入匹配房间
     * 返回 当前房间题组 + 排行榜
     */
    ResponseVO<Object> reenterToCompetition(Long userId);


    /**
     * 进行答题 推送排名信息
     */
    ResponseVO<Object> doAnswer(CompetitionDoAnswerParam param, Long userId);

}
