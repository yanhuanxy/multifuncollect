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
//        log.info("邮件发送入参日志：ProSmsVO:{},esReceivers:{},deptId:{}",vo,userVO,deptId);
//        ProSms proSms = new ProSms(null,vo.getEsTitle(), vo.getEsType(), vo.getEsReceiver(), vo.getEsParam(), vo.getEsContent(), vo.getEsSendTime(), vo.getEsSendStatus(), vo.getEsReadStatus(), vo.getRemark());
//        try {
//            setProSms(proSms);
//            if(ObjectUtils.isNotEmpty(userVO.getEmail())){
//
//                executor.execute(()->{
//                    //创建模版引擎工具
//                    TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
//                    //传入模版
//                    Template template = engine.getTemplate(vo.getEsContent());
//                    //传入参数 并生成html
//                    String htmlStr = template.render(JSONObject.parseObject(vo.getEsParam()));
//                    //调用邮件工具发送
//                    MailUtil.send(userVO.getEmail(), proSms.getEsTitle(), htmlStr, true);
//                });
//
//                proSms.setEsSendStatus(1);
//            }else {
//                proSms.setEsSendStatus(2);
//                smsService.save(proSms);
//                return Result.error("消息接收方，未设置邮件地址 邮件推送失败");
//            }
//        }catch (Exception e){
//            log.error("发送消息异常，错误信息{}",e.getMessage());
//            proSms.setEsSendStatus(2);
//            smsService.save(proSms);
//            return Result.error("发送消息异常，请联系管理员");
//        }
//        smsService.save(proSms);
//        return Result.ok();
//    }
//
//    @Override
//    @DS("#deptId")
//    public Result sendSms(ProSmsVO vo, SysUserVO userVO, Long deptId, File... files) {
//        log.info("邮件发送入参日志：ProSmsVO:{},esReceivers:{},deptId:{},files:{}",vo,userVO,deptId,files);
//        ProSms proSms = new ProSms(null,vo.getEsTitle(), vo.getEsType(), vo.getEsReceiver(), vo.getEsParam(), vo.getEsContent(), vo.getEsSendTime(), vo.getEsSendStatus(), vo.getEsReadStatus(), vo.getRemark());
//        try {
//            setProSms(proSms);
//            if(ObjectUtils.isNotEmpty(userVO.getEmail())){
//                executor.execute(()->{
//                    //创建模版引擎工具
//                    TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
//                    //传入模版
//                    Template template = engine.getTemplate(vo.getEsContent());
//                    //传入参数 并生成html
//                    String htmlStr = template.render(JSONObject.parseObject(vo.getEsParam()));
//                    //调用邮件工具发送
//                    MailUtil.send(userVO.getEmail(), proSms.getEsTitle(), htmlStr, true,files);
//                });
//
//                proSms.setEsSendStatus(1);
//            }else {
//                proSms.setEsSendStatus(2);
//                smsService.save(proSms);
//                return Result.error("消息接收方，未设置邮件地址 邮件推送失败");
//            }
//        }catch (Exception e){
//            log.info("邮件发送报错日志：错误信息:{}",e.getMessage());
//            proSms.setEsSendStatus(2);
//            smsService.save(proSms);
//            return Result.error("发送消息异常，请联系管理员");
//        }
//        smsService.save(proSms);
//        return Result.ok();
//    }
//
//    @Override
//    @DS("#deptId")
//    public Result sendSms(ProSmsVO vo, String[] esReceivers, Long deptId, File... files) {
//        log.info("邮件发送入参日志：ProSmsVO:{},esReceivers:{},deptId:{}", JSON.toJSONString(vo),JSON.toJSONString(esReceivers) ,deptId);
//        List<String> errorReceivers = new ArrayList<>();
//
//        ProSms proSms = new ProSms(null,vo.getEsTitle(), vo.getEsType(),String.join(",",esReceivers)  , vo.getEsParam(), vo.getEsContent(), vo.getEsSendTime(), vo.getEsSendStatus(), vo.getEsReadStatus(), vo.getRemark());
//        try {
//            setProSms(proSms);
//            if(ObjectUtils.isNotEmpty(String.join(",",esReceivers) )){
//                executor.execute(()->{
//                    //创建模版引擎工具
//                    TemplateEngine engine = TemplateUtil.createEngine(new TemplateConfig());
//                    //传入模版
//                    Template template = engine.getTemplate(vo.getEsContent());
//                    //传入参数 并生成html
//                    String htmlStr = template.render(JSONObject.parseObject(vo.getEsParam()));
//                    //调用邮件工具发送
//                    log.info("准备发送邮件：ProSmsVO:{},esReceivers:{}", JSON.toJSONString(vo),JSON.toJSONString(esReceivers) );
//                    MailUtil.send(Arrays.asList(esReceivers), vo.getEsTitle(), htmlStr, true,files);
//                });
//                proSms.setEsSendStatus(1);
//            }else {
//                proSms.setEsSendStatus(2);
//                smsService.save(proSms);
//                errorReceivers.add(String.join(",",esReceivers) );
//            }
//        }catch (Exception e){
//            log.info("邮件发送报错日志：错误信息:{},ProSmsVO:{},esReceivers:{}",e.getMessage(), JSON.toJSONString(vo),JSON.toJSONString(esReceivers));
//            proSms.setEsSendStatus(2);
//            smsService.save(proSms);
//            errorReceivers.add(String.join(",",esReceivers) );
//        }
//        log.info("邮件发送成功：ProSmsVO:{},esReceivers:{},deptId:{}", JSON.toJSONString(vo),JSON.toJSONString(esReceivers) ,deptId);
//        smsService.save(proSms);
//        return errorReceivers.size()==0?Result.ok("邮件发送成功"):Result.error("邮件发送失败");
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
