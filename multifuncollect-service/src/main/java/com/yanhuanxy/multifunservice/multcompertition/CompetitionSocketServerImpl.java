package com.yanhuanxy.multifunservice.multcompertition;

import com.yanhuanxy.multifuncommon.base.vo.ResponseVO;
import com.yanhuanxy.multifuncommon.enums.multcompertition.CompetitionStatusEnums;
import com.yanhuanxy.multifuncommon.enums.multcompertition.MessageTypeEnums;
import com.yanhuanxy.multifuncommon.exception.BaseRuntimeException;
import com.yanhuanxy.multifundomain.multcompertition.dto.CompetitionDoAnswerParam;
import com.yanhuanxy.multifundomain.multcompertition.vo.*;
import com.yanhuanxy.multifundomain.system.SysUser;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CompetitionSocketServerImpl implements CompetitionSocketServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(CompetitionSocketServerImpl.class);

//    @Resource
//    private TopicServiceImpl topicService;
//
//    @Resource
//    private CompetitionServer competitionServer;
//
    @Resource
    private SimpMessagingTemplate simpMessagingTemplate;
//
//    @Resource
//    private RedisManager redisManager;

    /**
     * 选择在线用户 分配房间
     * 1、分配房间 初始化房间数据 抽取试题
     *  1.1 无空闲房间 新用户创建等待房间 并加入
     *  1.2 有空闲房间 用户加入
     *  1.3 房间人满 初始化房间抽取试题 开始竞赛
     * 返回 当前房间题组 + 排行榜等等信息
     * @param userId 用户id
     */
    public ResponseVO<Object> chooseUserToCompetition(Long userId){

        ResponseVO<Object> responseVO = new ResponseVO<Object>();
        try{
            if(Objects.isNull(userId)){
                throw new BaseRuntimeException("请登录后再尝试");
            }

            Collection<CompetitionRoomInfoVO> competitionRoomInfoVOS = CurrentCachePool.curRoomPool.values();

            // 获取用户未满的房间
            CompetitionRoomInfoVO competitionRoomInfoVO = competitionRoomInfoVOS.stream().filter(item -> Objects.equals(item.getCompetitionStatus(), CompetitionStatusEnums.IDLE.getCode()))
                    .filter(item -> item.getRoomUsers().size() < CurrentCachePool.ROOM_USER_NUM).findFirst().orElse(null);
            if(competitionRoomInfoVO == null){
                // 创建等待房间 房间状态为 空闲中
                String roomNo = CurrentCachePool.ROOM_NO_PREFIX + System.currentTimeMillis();
                competitionRoomInfoVO = new CompetitionRoomInfoVO(CurrentCachePool.ROOM_USER_NUM);
                competitionRoomInfoVO.setRoomNo(roomNo);
                CurrentCachePool.curRoomPool.put(roomNo, competitionRoomInfoVO);
            }
            // 将用户加入房间
            Map<Long, CompetitionUserInfoVO> roomUsers = competitionRoomInfoVO.getRoomUsers();
            SysUser sysUser = CurrentCachePool.curUserPool.get(userId);
            CompetitionUserInfoVO competitionUserInfoVO = new CompetitionUserInfoVO(userId, sysUser.getNickname());
            roomUsers.put(userId, competitionUserInfoVO);

            LOGGER.warn(String.format("-----房间号：%s ----房间人数：%s", competitionRoomInfoVO.getRoomNo(), roomUsers.size()));
            if(roomUsers.size() == CurrentCachePool.ROOM_USER_NUM){
                // 人满初始化房间 开始答题
                initRoomTopicInfo(competitionRoomInfoVO);
            }

            responseVO.setCode(MessageTypeEnums.CHOOSE_USER.getCode().toString());
            responseVO.setMessage("匹配中请耐心等待!");
        }catch (BaseRuntimeException e){
            responseVO.setCode(MessageTypeEnums.ERROR.getCode().toString());
            responseVO.setMessage("匹配失败!".concat(e.getMessage()));
        }catch (Exception e){
            responseVO.setCode(MessageTypeEnums.ERROR.getCode().toString());
            responseVO.setMessage("匹配失败!");
            LOGGER.error("匹配失败!", e);
        }

        return responseVO;
    }

    /**
     * 初始化房间信息开始答题
     * 1、抽取题目
     * 2、设置第一题 计算超时时间
     * 3、初始化排行榜
     * 4、推送数据
     */
    private void initRoomTopicInfo(CompetitionRoomInfoVO competitionRoomInfoVO){

        // 抽题
        List<Object> topics = new ArrayList<>();
        competitionRoomInfoVO.setTopicsGroup(topics);
        // 设置第一题目
        LocalDateTime currentTime = LocalDateTime.now();
        // 保存 题目开始时间 题目结束时间 -> 追加一个延迟时间
        // 获取当前题目开始时间
        LocalDateTime answerStartTime = getNextActiveTopicStartTime(currentTime);
        // 获取当前题目结束时间
        LocalDateTime answerEndTime = getNextActiveTopicEndTime(answerStartTime);
        Long activeTopicId = 1L;//topics.get(0).getTopic().getId();
        competitionRoomInfoVO.getRoomUsers().values().forEach(item->{
            item.setActiveTopicId(activeTopicId);
            item.setActiveTopicStartTime(answerStartTime);
            item.setActiveTopicStartTime(answerEndTime);
        });
        competitionRoomInfoVO.setActiveRoomStartTime(currentTime);
        competitionRoomInfoVO.setActiveRoomEndTime(getActiveRoomEndTime(currentTime));
        // 比赛开始标记
        competitionRoomInfoVO.setCompetitionStatus(CompetitionStatusEnums.IN_COMPARE.getCode());
        // 存储房间失效标记
//        redisManager.expire(String.format(CurrentCachePool.ROOM_ALIVE_KEY, competitionRoomInfoVO.getRoomNo()), CurrentCachePool.ROOM_MAX_ALIVE_MINUTE * 60);

        //推送数据
        ResponseVO<Object> responseVO = new ResponseVO<Object>();
        responseVO.setCode(MessageTypeEnums.INIT_ROOM.getCode().toString());
        responseVO.setMessage("初始化房间数据");
        responseVO.setData(competitionRoomInfoVO);

        competitionRoomInfoVO.getRoomUsers().values().forEach(item->{
            simpMessagingTemplate.convertAndSendToUser(String.valueOf(item.getUserId()),"/multiple.initRoom", responseVO);
        });
    }

    /**
     * 计算房间最大时间 分钟
     * @param currentTime 当前时间
     */
    private LocalDateTime getActiveRoomEndTime(LocalDateTime currentTime){
        return currentTime.plusMinutes(CurrentCachePool.ROOM_MAX_ALIVE_MINUTE);
    }

    /**
     * 2、重连 重新进入房间 获取旧数据
     *   2.1 没找到记录 需要重新开始
     *   2.2 有记录 答题尚未开始
     *   2.3 有记录且正在游戏中
     *   2.3.1获取房间题目组
     *   2.3.2 获取房间排名
     *   2.3.3 获取当前正在作答的题目 题目结束时间 前端需要加个逻辑 当前时间大于题目结束时间 此题视为超时 切换下一题
     *   2.4 有记录 游戏已结束
     *   2.4.1 获取游戏记录信息
     */
    @Override
    public ResponseVO<Object> reenterToCompetition(Long userId) {

        ResponseVO<Object> responseVO = new ResponseVO<Object>();
        try {
            if(Objects.isNull(userId)){
                throw new BaseRuntimeException("请登录后再尝试");
            }
            CompetitionRoomInfoVO competitionRoomInfoVO = CurrentCachePool.curRoomPool.values().stream().filter(item ->
                    Objects.nonNull(item.getRoomUsers()) && item.getRoomUsers().containsKey(userId)).findAny().orElse(null);
            if(Objects.isNull(competitionRoomInfoVO)){
                throw new BaseRuntimeException("无答题记录!");
            }

            CompetitionStatusEnums competitionStatus = CompetitionStatusEnums.getCompetitionStatus(competitionRoomInfoVO.getCompetitionStatus());
            switch (competitionStatus){
                case IDLE:
                    responseVO.setMessage("答题尚未开始");
                    responseVO.setCode(MessageTypeEnums.CHOOSE_USER.getCode().toString());
                    break;
                case IN_COMPARE:
                    //组装 题目组 + 排名信息 + 当前正在做的题目
                    CompetitionRoomUserResultVO competitionRoomUserInfoVO = new CompetitionRoomUserResultVO();
                    competitionRoomUserInfoVO.setRoomNo(competitionRoomInfoVO.getRoomNo());
                    competitionRoomUserInfoVO.setUserId(userId);
                    competitionRoomUserInfoVO.setTopicsGroup(competitionRoomInfoVO.getTopicsGroup());
                    // 获取房间内已缓存的用户信息
                    setUserScoreRankInfo(userId, competitionRoomUserInfoVO, competitionRoomInfoVO);
                    responseVO.setMessage("重新加入竞赛");
                    responseVO.setCode(MessageTypeEnums.RE_ADD_USER.getCode().toString());
                    responseVO.setData(competitionRoomUserInfoVO);
                    break;
                case COMPARE_OVER:
                    // 通知答题结束 调用答题结果接口
                    responseVO.setMessage("答题已结束");
                    responseVO.setCode(MessageTypeEnums.ANSWER_END.getCode().toString());
                    break;
            }
        }catch (BaseRuntimeException e){
            responseVO.setCode(MessageTypeEnums.ERROR.getCode().toString());
            responseVO.setMessage("重新进入房间异常!".concat(e.getMessage()));
        }catch (Exception e){
            responseVO.setMessage("重新进入房间异常！");
            responseVO.setCode(MessageTypeEnums.ERROR.getCode().toString());
            LOGGER.error("重新进入失败！",e);
        }

        return responseVO;
    }

    /**
     * 获取当前用户所在房间的 用户分数排名信息
     */
    private void setUserScoreRankInfo(Long userId, CompetitionRoomUserResultVO roomUserInfoVO, CompetitionRoomInfoVO roomInfoVO){
        Map<Long, CompetitionUserInfoVO> roomUsers = roomInfoVO.getRoomUsers();
        // 对用户答题数据进行排序
        List<CompetitionUserScoreVO> userScoreRankInfo = getCompetitionUserScore(roomUsers);

        CompetitionUserInfoVO competitionUserInfoVO = roomUsers.get(userId);
        roomUserInfoVO.setUserScoreRankInfo(userScoreRankInfo);
        roomUserInfoVO.setScore(competitionUserInfoVO.getScore());
        roomUserInfoVO.setTotalScore(CurrentCachePool.TOPIC_TOTAL_SCORE);
        roomUserInfoVO.setUserName(competitionUserInfoVO.getUserName());
        // 当前正在答的题 题目有效期
        roomUserInfoVO.setActiveTopicId(competitionUserInfoVO.getActiveTopicId());
        roomUserInfoVO.setActiveTopicEndTime(competitionUserInfoVO.getActiveTopicEndTime());
    }

    /**
     * 获取用户排名信息
     * @param roomUsers 用户竞赛信息
     */
    private List<CompetitionUserScoreVO> getCompetitionUserScore(Map<Long, CompetitionUserInfoVO> roomUsers){
        return roomUsers.values().stream().map(item -> {
            CompetitionUserScoreVO competitionUserScoreVO = new CompetitionUserScoreVO();
            competitionUserScoreVO.setUserId(item.getUserId());
            competitionUserScoreVO.setUserName(item.getUserName());
            competitionUserScoreVO.setScore(item.getScore());
            return competitionUserScoreVO;
        }).sorted(Comparator.comparing(CompetitionUserScoreVO::getScore)).collect(Collectors.toList());
    }

    /**
     * 答题
     * 1、更新提交信息
     * 2、审题与计算得分
     * 3、刷新比赛信息
     * 3.1 推送排名信息
     * 3.2 预先100分 比赛结束
     *
     * @param param 参数
     */
    @Override
    public ResponseVO<Object> doAnswer(CompetitionDoAnswerParam param, Long userId) {

        ResponseVO<Object> responseVO = new ResponseVO<Object>();
        try{
            if(Objects.isNull(userId)){
                throw new BaseRuntimeException("请登录后再尝试");
            }
            CompetitionRoomInfoVO competitionRoomInfoVO = CurrentCachePool.curRoomPool.get(param.getRoomNo());
            CompetitionUserInfoVO competitionUserInfoVO = competitionRoomInfoVO.getRoomUsers().get(userId);
            List<CompetitionAnswerVO> answerGroup = competitionUserInfoVO.getAnswerGroup();
            if(Objects.isNull(answerGroup)){
                answerGroup = new ArrayList<>();
                competitionUserInfoVO.setAnswerGroup(answerGroup);
            }
            List<Object> topicsGroup = competitionUserInfoVO.getTopicsGroup();
//            TopicGetVO topicGetVO = topicsGroup.stream().filter(item -> Objects.equals(item.getTopic().getId(), param.getTopicId())).findAny().get();

            // 记录用户答题信息
            CompetitionAnswerVO competitionAnswerVO = new CompetitionAnswerVO();
            competitionAnswerVO.setId(param.getTopicId());
            competitionAnswerVO.setType(param.getType());
            competitionAnswerVO.setAnswer(param.getAnswer());
            // 审题
//            TopicAnswerParam topicAnswerParam = new TopicAnswerParam();
//            topicAnswerParam.setId(param.getTopicId());
//            topicAnswerParam.setType(param.getType());
//            topicAnswerParam.setAnswer(param.getAnswer());
//            boolean isTrue = topicService.answerContrast(topicGetVO.getTopic(), topicAnswerParam);
            competitionAnswerVO.setResult(true);
            competitionAnswerVO.setActiveAnswerTime(param.getActiveAnswerTime());
            competitionAnswerVO.setActiveAnswerStartTime(competitionUserInfoVO.getActiveTopicStartTime());
            LocalDateTime activeTopicEndTime = competitionUserInfoVO.getActiveTopicEndTime();
            competitionAnswerVO.setActiveAnswerEndTime(activeTopicEndTime);
            // 计算是否超时
            boolean notTimeout = calcUserAnswerIsNotTimeout(param.getActiveAnswerTime(), activeTopicEndTime);
            answerGroup.add(competitionAnswerVO);
            // 更新下一题的开始时间结束时间
            updateNextActiveTopicStartAndEndTime(param.getActiveAnswerTime(), competitionUserInfoVO);

            if(true && notTimeout){
                // 计算得分
                Integer userScore = competitionUserInfoVO.getScore();
                competitionUserInfoVO.setScore((userScore + CurrentCachePool.TOPIC_SCORE));
            }
            LOGGER.warn("-----房间号-: {} 用户: {} 分数: {}", competitionRoomInfoVO.getRoomNo(), competitionUserInfoVO.getUserName(), competitionUserInfoVO.getScore());
            if(Objects.equals(competitionUserInfoVO.getScore(), CurrentCachePool.TOPIC_TOTAL_SCORE)){
                // 保存比赛记录
//                competitionServer.saveCompetitionRecord(competitionRoomInfoVO, LocalDateTime.now());
                // 比赛结束标记
                competitionRoomInfoVO.setCompetitionStatus(CompetitionStatusEnums.COMPARE_OVER.getCode());
                responseVO.setMessage("答题结束！");
                responseVO.setCode(MessageTypeEnums.ANSWER_END.getCode().toString());
            }else{
                // 更新排名
                CompetitionRoomUserResultVO competitionRoomUserInfoVO = new CompetitionRoomUserResultVO();
                competitionRoomUserInfoVO.setRoomNo(competitionRoomInfoVO.getRoomNo());
                competitionRoomUserInfoVO.setUserId(userId);
                // 获取房间内已缓存的用户信息
                setUserScoreRankInfo(userId, competitionRoomUserInfoVO, competitionRoomInfoVO);

                ResponseVO<Object> responseRankInfoVO = new ResponseVO<Object>();
                responseRankInfoVO.setCode(MessageTypeEnums.DO_ANSWER.getCode().toString());
                responseRankInfoVO.setMessage("排名信息");

                simpMessagingTemplate.convertAndSend("/wsTopic/".concat(String.valueOf(param.getRoomNo()).concat("/multiple.userRankInfo")), responseRankInfoVO);
                responseVO.setMessage("作答完成！");
                responseVO.setCode(MessageTypeEnums.DO_ANSWER.getCode().toString());
            }
        }catch (BaseRuntimeException e){
            responseVO.setCode(MessageTypeEnums.ERROR.getCode().toString());
            responseVO.setMessage("答题时审计异常!".concat(e.getMessage()));
        }catch (Exception e){
            responseVO.setMessage("答题时审计异常！");
            responseVO.setCode(MessageTypeEnums.ERROR.getCode().toString());
            LOGGER.error("答题时审计异常！", e);
        }
        return responseVO;
    }

    /**
     * 计算有效得分 -> 作答时间在结束时间之前
     */
    private boolean calcUserAnswerIsNotTimeout(LocalDateTime activeAnswerTime, LocalDateTime activeTopicEndTime){

        return activeAnswerTime.isBefore(activeTopicEndTime);
    }

    /**
     * 更新当前题目的有效开始结束时间
     * @param activeAnswerTime 当前时间
     * @param competitionUserInfoVO 用户信息
     */
    private void updateNextActiveTopicStartAndEndTime(LocalDateTime activeAnswerTime, CompetitionUserInfoVO competitionUserInfoVO){
        // 获取当前题目开始时间
        LocalDateTime answerStartTime = getNextActiveTopicStartTime(activeAnswerTime);
        // 获取当前题目结束时间
        LocalDateTime answerEndTime = getNextActiveTopicEndTime(answerStartTime);

        competitionUserInfoVO.setActiveTopicStartTime(answerStartTime);
        competitionUserInfoVO.setActiveTopicEndTime(answerEndTime);
    }

    /**
     * 计算下一题的有效开始时间
     * @param currentTime 当前时间
     */
    private LocalDateTime getNextActiveTopicStartTime(LocalDateTime currentTime){

        return currentTime.plusSeconds(CurrentCachePool.TOPIC_DELAY_SECONDS);
    }

    /**
     * 计算下一题的有效结束时间
     * @param currentTime 当前时间
     */
    private LocalDateTime getNextActiveTopicEndTime(LocalDateTime currentTime){

        return currentTime.plusSeconds(CurrentCachePool.TOPIC_INTERVAL_TIME_SECONDS + CurrentCachePool.TOPIC_DELAY_SECONDS);
    }

    @MessageExceptionHandler(Exception.class)
    public String handleExceptions(Exception t){
        LOGGER.error("【websocket 异常】",t);
        return t.getMessage();
    }
}
