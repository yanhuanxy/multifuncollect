package com.yanhuanxy.multifunservice.multcompertition;

import com.yanhuanxy.multifundomain.multcompertition.vo.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class CompetitionServerImpl implements CompetitionServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionServerImpl.class);


    @Override
    public CompetitionUserResultVO answerCompleted(String roomNo) {

        // 校验房间是否已过期 获取用户信息
        Long userId =  checkRoomIsExistAndGetUserId(roomNo);

        CompetitionUserResultVO competitionUserResultVO = new CompetitionUserResultVO();
        CompetitionRoomInfoVO competitionRoomInfoVO = CurrentCachePool.curRoomPool.get(roomNo);
        // 获取用户竞赛排名信息
        Map<Long, CompetitionUserInfoVO> roomUsers = competitionRoomInfoVO.getRoomUsers();
        CompetitionUserInfoVO currentUserInfo = roomUsers.get(userId);
        List<CompetitionUserScoreDetailVO> userScoreRankInfo = roomUsers.values().stream().map(item -> {
            CompetitionUserScoreDetailVO competitionUserScoreDetailVO = new CompetitionUserScoreDetailVO();
            competitionUserScoreDetailVO.setUserId(item.getUserId());
            competitionUserScoreDetailVO.setUserName(item.getUserName());
            competitionUserScoreDetailVO.setScore(item.getScore());
            List<CompetitionAnswerVO> answerGroup = item.getAnswerGroup();
            competitionUserScoreDetailVO.setTotalTopic(answerGroup.size());
            long rightTopic = answerGroup.stream().filter(CompetitionAnswerVO::isResult).count();
            competitionUserScoreDetailVO.setRightTopic(Long.valueOf(rightTopic).intValue());
            return competitionUserScoreDetailVO;
        }).sorted(Comparator.comparing(CompetitionUserScoreDetailVO::getScore).reversed()).collect(Collectors.toList());
        competitionUserResultVO.setUserScoreRankInfo(userScoreRankInfo);
        competitionUserResultVO.setUserName(currentUserInfo.getUserName());
        // 获取当前用户 排名积分
        Integer currentUserIntegral = getCurrentUserIntegral(userScoreRankInfo, userId);
        competitionUserResultVO.setIntegral(currentUserIntegral);
        try {
            // 获取用户总积分
            Integer integral = 1;//integralService.getIntegral();
            competitionUserResultVO.setTotalIntegral(integral);
        }catch (Exception e){
            LOGGER.error("用户获取竞赛信息时获取用户总积分异常！", e);
        }
        return competitionUserResultVO;
    }

    /**
     * 获取当前用户的排名积分
     * @param userScoreRankInfo 排名信息
     * @param userId 用户id
     */
    private Integer getCurrentUserIntegral(List<CompetitionUserScoreDetailVO> userScoreRankInfo, Long userId){
        // 获取当前用户排名对应的积分
        int index = -1;
        for (CompetitionUserScoreDetailVO competitionUserScoreDetailVO : userScoreRankInfo) {
            index++;
            if(Objects.equals(competitionUserScoreDetailVO.getUserId(), userId)){
                if(Objects.equals(competitionUserScoreDetailVO.getScore(),0)){
                    index =-1;
                }
                break;
            }
        }
        return calcCurrentUserIntegral(index);
    }

    /**
     * 获取当前用户所在排名能获得的积分
     * @param index 排名
     */
    private Integer calcCurrentUserIntegral(int index){
        Integer[] roomUserRankIntegral = CurrentCachePool.ROOM_USER_RANK_INTEGRAL;
        if(index >= roomUserRankIntegral.length || index == -1){
            return 0;
        }
        return roomUserRankIntegral[index];
    }

    @Override
    public CompetitionUserTopicsVO answerRecord(String roomNo) {

        // 校验房间是否已过期 获取用户信息
        Long userId =  checkRoomIsExistAndGetUserId(roomNo);

        CompetitionRoomInfoVO competitionRoomInfoVO = CurrentCachePool.curRoomPool.get(roomNo);
        CompetitionUserInfoVO competitionUserInfoVO = competitionRoomInfoVO.getRoomUsers().get(userId);
        CompetitionUserTopicsVO competitionUserTopicsVO = new CompetitionUserTopicsVO();
        competitionUserTopicsVO.setUserId(userId);
        competitionUserTopicsVO.setUserName(competitionUserInfoVO.getUserName());
        // 根据用户已答的题id 获取完整的题信息
        List<CompetitionAnswerVO> userAnswerGroup = competitionUserInfoVO.getAnswerGroup();
        List<Long> topicId = userAnswerGroup.stream().map(CompetitionAnswerVO::getId).collect(Collectors.toList());
        List<Object> userTopicGetVO = new ArrayList<>(); //competitionUserInfoVO.getTopicsGroup().stream().filter(item -> topicId.contains(item.getTopic().getId())).collect(Collectors.toList());
        competitionUserTopicsVO.setTopicsGroup(userTopicGetVO);
        competitionUserTopicsVO.setAnswerGroup(userAnswerGroup);

        return competitionUserTopicsVO;
    }

    /**
     * 校验房间是否已过期 以及用户信息
     * @param roomNo 房间号
     */
    private Long checkRoomIsExistAndGetUserId(String roomNo){
        // 存储房间失效标记
//        long expire = redisManager.getExpire(String.format(CurrentCachePool.ROOM_ALIVE_KEY, roomNo));
//        if(expire < 0){
//            throw new BaseRuntimeException("房间不存在！");
//        }
//        Long userId = SecurityUtils.getUserId();
//        if(Objects.isNull(userId)){
//            throw new BaseRuntimeException("无用户信息");
//        }
        return 1L;
    }


    /**
     *
     * @param competitionRoomInfoVO 用户比赛记录信息
     */
    @Async
    public void saveCompetitionRecord(CompetitionRoomInfoVO competitionRoomInfoVO, LocalDateTime currentDateTime){
        Map<Long, CompetitionUserInfoVO> roomUsers = competitionRoomInfoVO.getRoomUsers();
        // 保存答题记录
        List<CompetitionUserInfoVO> userRankInfo = roomUsers.values().stream().sorted(Comparator.comparing(CompetitionUserInfoVO::getScore).reversed()).collect(Collectors.toList());
        IntStream.range(0,userRankInfo.size()).forEach(index->{
            CompetitionUserInfoVO competitionUserInfoVO = userRankInfo.get(index);
            int tmpIndex = index;
            // 获取用户排名积分
            if(Objects.equals(competitionUserInfoVO.getScore(), 0)){
                tmpIndex = -1;
            }
            Integer integral = calcCurrentUserIntegral(tmpIndex);
//            CompetitionRecord competitionRecord = new CompetitionRecord();
            try{
//                competitionRecord.setType(1);
//                competitionRecord.setUserId(competitionUserInfoVO.getUserId());
//                competitionRecord.setUserName(competitionUserInfoVO.getUserName());
//                List<CompetitionAnswerVO> answerGroup = competitionUserInfoVO.getAnswerGroup();
//                competitionRecord.setTopicNum(answerGroup.size());
//                long rightNum = answerGroup.stream().filter(CompetitionAnswerVO::isResult).count();
//                competitionRecord.setCorrectNum(Long.valueOf(rightNum).intValue());
//                competitionRecord.setScore(competitionUserInfoVO.getScore());
//                competitionRecord.setRank(index+1);
//                competitionRecord.setIntegral(integral);
//                competitionRecord.setCompleteTime(currentDateTime);
//                competitionRecordManager.save(competitionRecord);
            }catch (Exception e){
                LOGGER.error("多人竞答用户答题记录保存失败！记录信息: {}", "GsonUtils.toJson(competitionRecord)");
            }finally {
                // 变更用户积分
                boolean isSuccess = false;
                int count = 0;
                while (!isSuccess && count<3){
                    try {
                        isSuccess = false; //integralService.updateIntegral(integral, IntegralTypeEnums.ADD.getCode());
                    }catch (Exception e){
                        LOGGER.error("多人竞答调用积分变更接口异常！", e);
                    }
                    count++;
                }
                if(!isSuccess){
                    LOGGER.error("多人竞答用户积分变更失败！用户: {} 用户id:{} 积分: {} ", competitionUserInfoVO.getUserId(), competitionUserInfoVO.getUserName(), integral);
                }
            }
        });
    }
}
