package com.yanhuanxy.multifunservice.message.impl;//package com.yanhuanx.demo.page1.service.message.impl;
//
//import cn.hutool.extra.mail.MailUtil;
//import cn.hutool.extra.template.Template;
//import cn.hutool.extra.template.TemplateConfig;
//import cn.hutool.extra.template.TemplateEngine;
//import cn.hutool.extra.template.TemplateUtil;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.JSONObject;
//import com.baomidou.dynamic.datasource.annotation.DS;
//import com.google.common.util.concurrent.ThreadFactoryBuilder;
//import com.kelven.dep.domain.message.ProSms;
//import com.kelven.dep.sdk.entity.Result;
//import com.kelven.dep.sdk.vo.ProSmsVO;
//import com.kelven.dep.service.message.ProSmsService;
//import com.kelven.dep.service.message.StrategySms;
//import com.kelven.user.center.sdk.vo.SysUserVO;
//import com.yanhuanx.demo.page1.service.message.ProSmsService;
//import com.yanhuanx.demo.page1.service.message.StrategySms;
//import org.apache.commons.lang3.ObjectUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Date;
//import java.util.List;
//import java.util.concurrent.ArrayBlockingQueue;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * @author
// * @date 2021/8/2
// */
//@Component("email")
//public class StrategySmsEmailImpl implements StrategySms {
//    private Logger log = LoggerFactory.getLogger(StrategySmsEmailImpl.class);
//    private final ProSmsService smsService;
//
//    private ExecutorService executor =  new ThreadPoolExecutor(10, 50, 1, TimeUnit.SECONDS, new ArrayBlockingQueue(10),new ThreadFactoryBuilder().setNameFormat("sendEmail-task-%d").build());
//
//    @Autowired
//    public StrategySmsEmailImpl(ProSmsService smsService) {
//        this.smsService = smsService;
//    }
//    @Override
//    @DS("#deptId")
//    public Result sendSms(ProSmsVO vo,SysUserVO userVO,Long deptId) {
//        log.info("???????????????????????????ProSmsVO:{},esReceivers:{},deptId:{}",vo,userVO,deptId);
//        ProSms proSms = new ProSms(null,vo.getEsTitle(), vo.getEsType(), vo.getEsReceiver(), vo.getEsParam(), vo.getEsContent(), vo.getEsSendTime(), vo.getEsSendStatus(), vo.getEsReadStatus(), vo.getRemark());
//        try {
//            setProSms(proSms);
//            if(ObjectUtils.isNotEmpty(userVO.getEmail())){
//
//                executor.execute(()->{
//                    //????????????????????????
//                    TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
//                    //????????????
//                    Template template = engine.getTemplate(vo.getEsContent());
//                    //???????????? ?????????html
//                    String htmlStr = template.render(JSONObject.parseObject(vo.getEsParam()));
//                    //????????????????????????
//                    MailUtil.send(userVO.getEmail(), proSms.getEsTitle(), htmlStr, true);
//                });
//
//                proSms.setEsSendStatus(1);
//            }else {
//                proSms.setEsSendStatus(2);
//                smsService.save(proSms);
//                return Result.error("??????????????????????????????????????? ??????????????????");
//            }
//        }catch (Exception e){
//            log.error("?????????????????????????????????{}",e.getMessage());
//            proSms.setEsSendStatus(2);
//            smsService.save(proSms);
//            return Result.error("???????????????????????????????????????");
//        }
//        smsService.save(proSms);
//        return Result.ok();
//    }
//
//    @Override
//    @DS("#deptId")
//    public Result sendSms(ProSmsVO vo, SysUserVO userVO, Long deptId, File... files) {
//        log.info("???????????????????????????ProSmsVO:{},esReceivers:{},deptId:{},files:{}",vo,userVO,deptId,files);
//        ProSms proSms = new ProSms(null,vo.getEsTitle(), vo.getEsType(), vo.getEsReceiver(), vo.getEsParam(), vo.getEsContent(), vo.getEsSendTime(), vo.getEsSendStatus(), vo.getEsReadStatus(), vo.getRemark());
//        try {
//            setProSms(proSms);
//            if(ObjectUtils.isNotEmpty(userVO.getEmail())){
//                executor.execute(()->{
//                    //????????????????????????
//                    TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
//                    //????????????
//                    Template template = engine.getTemplate(vo.getEsContent());
//                    //???????????? ?????????html
//                    String htmlStr = template.render(JSONObject.parseObject(vo.getEsParam()));
//                    //????????????????????????
//                    MailUtil.send(userVO.getEmail(), proSms.getEsTitle(), htmlStr, true,files);
//                });
//
//                proSms.setEsSendStatus(1);
//            }else {
//                proSms.setEsSendStatus(2);
//                smsService.save(proSms);
//                return Result.error("??????????????????????????????????????? ??????????????????");
//            }
//        }catch (Exception e){
//            log.info("???????????????????????????????????????:{}",e.getMessage());
//            proSms.setEsSendStatus(2);
//            smsService.save(proSms);
//            return Result.error("???????????????????????????????????????");
//        }
//        smsService.save(proSms);
//        return Result.ok();
//    }
//
//    @Override
//    @DS("#deptId")
//    public Result sendSms(ProSmsVO vo, String[] esReceivers, Long deptId, File... files) {
//        log.info("???????????????????????????ProSmsVO:{},esReceivers:{},deptId:{}", JSON.toJSONString(vo),JSON.toJSONString(esReceivers) ,deptId);
//        List<String> errorReceivers = new ArrayList<>();
//
//        ProSms proSms = new ProSms(null,vo.getEsTitle(), vo.getEsType(),String.join(",",esReceivers)  , vo.getEsParam(), vo.getEsContent(), vo.getEsSendTime(), vo.getEsSendStatus(), vo.getEsReadStatus(), vo.getRemark());
//        try {
//            setProSms(proSms);
//            if(ObjectUtils.isNotEmpty(String.join(",",esReceivers) )){
//                executor.execute(()->{
//                    //????????????????????????
//                    TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
//                    //????????????
//                    Template template = engine.getTemplate(vo.getEsContent());
//                    //???????????? ?????????html
//                    String htmlStr = template.render(JSONObject.parseObject(vo.getEsParam()));
//                    //????????????????????????
//                    log.info("?????????????????????ProSmsVO:{},esReceivers:{}", JSON.toJSONString(vo),JSON.toJSONString(esReceivers) );
//                    MailUtil.send(Arrays.asList(esReceivers), vo.getEsTitle(), htmlStr, true,files);
//                });
//                proSms.setEsSendStatus(1);
//            }else {
//                proSms.setEsSendStatus(2);
//                smsService.save(proSms);
//                errorReceivers.add(String.join(",",esReceivers) );
//            }
//        }catch (Exception e){
//            log.info("???????????????????????????????????????:{},ProSmsVO:{},esReceivers:{}",e.getMessage(), JSON.toJSONString(vo),JSON.toJSONString(esReceivers));
//            proSms.setEsSendStatus(2);
//            smsService.save(proSms);
//            errorReceivers.add(String.join(",",esReceivers) );
//        }
//        log.info("?????????????????????ProSmsVO:{},esReceivers:{},deptId:{}", JSON.toJSONString(vo),JSON.toJSONString(esReceivers) ,deptId);
//        smsService.save(proSms);
//        return errorReceivers.size()==0?Result.ok("??????????????????"):Result.error("??????????????????");
//    }
//
//    private void setProSms(ProSms proSms) {
//        proSms.setEsReadStatus(0);
//        proSms.setEsSendTime(new Date());
//        if(ObjectUtils.isEmpty(proSms.getCreator())){
//            proSms.setCreator(DEFAULT_ADMIN);
//            proSms.setModifyer(DEFAULT_ADMIN);
//        }
//        proSms.setCreateTime(new Date());
//        proSms.setModifyTime(new Date());
//    }
//
//
//}
