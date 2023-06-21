package com.yanhuanxy.multifunservice.multcompertition;

import com.yanhuanxy.multifundomain.multcompertition.vo.CompetitionRoomInfoVO;
import com.yanhuanxy.multifundomain.multcompertition.vo.CompetitionUserResultVO;
import com.yanhuanxy.multifundomain.multcompertition.vo.CompetitionUserTopicsVO;

import java.time.LocalDateTime;

public interface CompetitionServer {


    /**
     * 答题完毕
     * 返回 获取比赛结果信息
     */
    CompetitionUserResultVO answerCompleted(String roomNo);

    /**
     * 答题完毕
     * 返回 获取比赛答题记录信息
     */
    CompetitionUserTopicsVO answerRecord(String roomNo);


    /**
     * 答题完毕
     * 保存 比赛答题记录信息
     */
    void saveCompetitionRecord(CompetitionRoomInfoVO competitionRoomInfoVO, LocalDateTime currentDateTime);

}
