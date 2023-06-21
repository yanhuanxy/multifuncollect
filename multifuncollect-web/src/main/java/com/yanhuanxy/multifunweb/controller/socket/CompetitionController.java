package com.yanhuanxy.multifunweb.controller.socket;


import com.yanhuanxy.multifuncommon.base.vo.ResponseVO;
import com.yanhuanxy.multifundomain.multcompertition.dto.CompetitionDoAnswerParam;
import com.yanhuanxy.multifunservice.multcompertition.CompetitionServer;
import com.yanhuanxy.multifunservice.multcompertition.CompetitionSocketServer;
import jakarta.annotation.Resource;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * 多人竞赛 webSocket api
 * @author yym
 * @date 2023/05/31
 */

@RestController
public class CompetitionController {

    @Resource
    private CompetitionSocketServer competitionSocketServer;

    @Resource
    private CompetitionServer competitionServer;

    @MessageMapping("multiple.start")
    @SendToUser("goTo/multiple.start")
    public ResponseVO<Object> chooseUserToCompetition(SimpMessageHeaderAccessor headerAccessor){

        return competitionSocketServer.chooseUserToCompetition(Long.valueOf(Objects.requireNonNull(headerAccessor.getUser()).getName()));
    }

    @SubscribeMapping("multiple.reenter")
    public ResponseVO<Object> reenterToCompetition(SimpMessageHeaderAccessor headerAccessor){

        return competitionSocketServer.reenterToCompetition(Long.valueOf(Objects.requireNonNull(headerAccessor.getUser()).getName()));
    }

    @MessageMapping("multiple.doAnswer")
    public ResponseVO<Object> doAnswer(@Payload CompetitionDoAnswerParam param, SimpMessageHeaderAccessor headerAccessor){
        return competitionSocketServer.doAnswer(param, Long.valueOf(Objects.requireNonNull(headerAccessor.getUser()).getName()));
    }

//    @GetMapping("/multiple/answerCompleted/{roomNo}")
//    public ResponseVO<CompetitionUserResultVO> answerCompleted(@RequestPart(name = "roomNo") String roomNo){
//
//        CompetitionUserResultVO competitionUserResultVO = competitionServer.answerCompleted(roomNo);
//
//         return Result.success(competitionUserResultVO);
//    }
//
//    @GetMapping("/multiple/answerRecord")
//    public ResponseVO<Object> answerRecord(@RequestPart(name = "roomNo") String roomNo){
//
//        CompetitionUserTopicsVO competitionUserTopicsVO = competitionServer.answerRecord(roomNo);
//
//        return Result.success(competitionUserTopicsVO);
//    }
}
