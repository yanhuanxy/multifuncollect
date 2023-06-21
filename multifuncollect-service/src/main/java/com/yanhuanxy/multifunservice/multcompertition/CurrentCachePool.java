package com.yanhuanxy.multifunservice.multcompertition;


import com.yanhuanxy.multifundomain.multcompertition.vo.CompetitionRoomInfoVO;
import com.yanhuanxy.multifundomain.system.SysUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 当前缓存池
 */
@Component
public class CurrentCachePool {
    private static final Logger LOGGER = LoggerFactory.getLogger(CurrentCachePool.class);

    // 房间信息
    public static Map<String, CompetitionRoomInfoVO> curRoomPool = new ConcurrentHashMap<>();

    // 在线用户
    public static Map<Long, SysUser> curUserPool = new ConcurrentHashMap<>();

    /**
     * 房间号前缀
     */
    public static final String ROOM_NO_PREFIX = "ROOM_";

    /**
     * 房间存活key
     */
    public static final String ROOM_ALIVE_KEY = "multcompertition:%s:live";

    /**
     * 题目间隔时间
     */
    @Value("${competition.interval-time}")
    public static final Long TOPIC_INTERVAL_TIME_SECONDS = 30L;

    /**
     * 题目延迟秒数
     */
    @Value("${competition.TOPIC-DELAY-SECONDS}")
    public static final Long TOPIC_DELAY_SECONDS = 1L;

    /**
     * 题目分数
     */
    @Value("${competition.topic-score}")
    public static final Integer TOPIC_SCORE = 20;

    /**
     * 题目总分
     */
    @Value("${competition.topic-total-score}")
    public static final Integer TOPIC_TOTAL_SCORE = 100;

    /**
     * 房间人数
     */
    @Value("${competition.user-num}")
    public static final Integer ROOM_USER_NUM = 5;

    /**
     * 房间最大题数
     */
    @Value("${competition.room-topic-max-num}")
    public static final Integer ROOM_MAX_TOPIC_NUM = 5;

    /**
     * 房间最大存活时间
     */
    @Value("${competition.room-max-alive-minute}")
    public static final Integer ROOM_MAX_ALIVE_MINUTE = 15;

    /**
     * 竞赛排名积分对照关系
     */
    @Value("${competition.room-user-rank-integral}")
    public static final Integer[] ROOM_USER_RANK_INTEGRAL = new Integer[]{10,5,2};


    /**
     * 清除过期房间记录
     * @param redisManager redis
     */
//    public static void cleanTimeoutRoom(RedisManager redisManager){
//        LocalDateTime localDateTime = LocalDateTime.now();
//        // 过滤超时房间
//        List<CompetitionRoomInfoVO> timeoutRoomNum = curRoomPool.values().stream().filter(roomNo -> localDateTime.isAfter(localDateTime)
//                        && redisManager.getExpire(String.format(ROOM_ALIVE_KEY, roomNo)) < 0).collect(Collectors.toList());
//        if(timeoutRoomNum.isEmpty()){
//           return;
//        }
//
//        LOGGER.info("当前需清除的房间号: {}", GsonUtils.toJson(timeoutRoomNum));
//        // 清除
//        timeoutRoomNum.forEach(tmpRoomInfo->{
//            curRoomPool.remove(tmpRoomInfo.getRoomNo());
//            cleanTimeoutRoomUser(tmpRoomInfo.getRoomUsers().keySet());
//        });
//    }

    /**
     * 清除过期房间内已建立连接用户记录
     */
    private static void cleanTimeoutRoomUser(Set<Long> userIds){
        if(userIds.isEmpty()){
            return;
        }
        userIds.forEach(userId->{
            curUserPool.remove(userId);
        });
    }

}
