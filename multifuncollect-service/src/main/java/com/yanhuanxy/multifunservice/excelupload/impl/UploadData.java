package com.yanhuanxy.multifunservice.excelupload.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableMap;
import com.yanhuanxy.multifuncommon.enums.FileTypeEnum;
import com.yanhuanxy.multifunexport.tools.util.BatchDecomUtil;
import com.yanhuanxy.multifundao.desfrom.DesFormDataBatchUploadMapper;
import com.yanhuanxy.multifundomain.desfrom.dto.DesTableColumnDTO;
import com.yanhuanxy.multifundomain.excelupload.dto.UploadDataExcelDTO;
import com.yanhuanxy.multifundomain.excelupload.dto.UploadDataFileDTO;
import com.yanhuanxy.multifundomain.excelupload.dto.UploadMoreDataConfDTO;
import com.yanhuanxy.multifundomain.excelupload.entity.UploadMoreDataConf;
import com.yanhuanxy.multifundomain.excelupload.entity.UploadMoreDataMain;
import com.yanhuanxy.multifundomain.message.vo.ProSmsVO;
import com.yanhuanxy.multifundomain.system.SysUser;
import com.yanhuanxy.multifunexport.fileservice.util.OssUtils;
import com.yanhuanxy.multifunexport.fileservice.util.Result;
import com.yanhuanxy.multifunexport.tools.excel.read.BatchReaderExcelUtil;
import com.yanhuanxy.multifunexport.tools.util.ThreadPoolUtils;
import com.yanhuanxy.multifunservice.message.StrategySms;
import com.yanhuanxy.multifunservice.putstorage.PutStorageStrategy;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

@Service
public class UploadData {
    private static final Logger logger = LoggerFactory.getLogger(UploadData.class);

    private final SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private final StrategySms strategySms;

    private final SqlSessionFactory sqlSessionFactory;

    private final Map<String, PutStorageStrategy> putStorageStrategyMap;

    private final DesFormDataBatchUploadMapper desFormDataBatchUploadMapper;

    public UploadData(StrategySms strategySms, SqlSessionFactory sqlSessionFactory,
                      Map<String, PutStorageStrategy> putStorageStrategyMap,
                      DesFormDataBatchUploadMapper desFormDataBatchUploadMapper) {
        this.strategySms = strategySms;
        this.sqlSessionFactory = sqlSessionFactory;
        this.putStorageStrategyMap = putStorageStrategyMap;
        this.desFormDataBatchUploadMapper = desFormDataBatchUploadMapper;
    }

    /**
     * 类型
     */
    private final static String MAIN_UPLOAD_TYPE = "1";

    /**
     * 单文件时 Key
     */
    private final static String SINGLE_FILE_KEY = "onlyone";

    /**
     * 主配置
     */
    private UploadMoreDataMain uploadMoreDataMain;

    /**
     * 配置
     */
    private List<UploadMoreDataConf> moreDataConfList;


    public void setUploadMoreDataMain(UploadMoreDataMain uploadMoreDataMain) {
        this.uploadMoreDataMain = uploadMoreDataMain;
    }

    public void setMoreDataConfList(List<UploadMoreDataConf> moreDataConfList) {
        this.moreDataConfList = moreDataConfList;
    }

    /**
     * 获取上传文件 -> 单个文件
     *           -> 文件压缩包 -> 解压 -> 文件集合
     * 根据配置 读取文件
     * 文件 -> 一个文件对应一个配置  -> 边读边写
     *     -> 一个文件对应多个配置
     * 数据入库 -> 数据写入 -> max 写入一次 -> 重复执行
     *        -> 异常 直接结束？ 返回错误信息
     *        -> 站内信提醒
     */

    public Result<?> handUploadFile(MultipartFile multipartFile, Long mainId, String curUploadDate){
        String fileName = multipartFile.getOriginalFilename();
        boolean exist = FileTypeEnum.exist(fileName);
        if(!exist){
            logger.error("配置 {} 上传文件类型错误。数据文件只能为（.xls,.xlsx,.csv,.zip）", mainId);
            return Result.error("数据文件只能为（.xls,.xlsx,.csv,.zip）");
        }
        uploadMoreDataMain.setCurUploadDate(curUploadDate);

        if(ObjectUtils.isEmpty(uploadMoreDataMain.getDateConfType())){
            uploadMoreDataMain.setDateConfType(0);
        }
        String mainUploadType = uploadMoreDataMain.getMainUploadType().trim();
        boolean isZip = FileTypeEnum.isZip(fileName);
        if(Objects.equals("1", mainUploadType) && isZip){
            logger.error("配置{} 上传文件类型与配置不符。单文件数据文件只能为（.xls,.xlsx,.csv）", mainId);
            return Result.error("单文件数据文件只能为（.xls,.xlsx,.csv）");
        }else if((Objects.equals("2", mainUploadType)  || Objects.equals("3", mainUploadType) ) && !isZip){
            logger.error("配置{} 上传文件类型与配置不符。多文件数据文件只能为（.zip）", mainId);
            return Result.error("多文件数据文件只能为（.zip）");
        }
        // 转存 解压 上传文件
        List<UploadDataFileDTO> multipartFileMap = handleUploadDataFileToDto(isZip, multipartFile, fileName);
        // 根据文件名称对配置信息分组
        Map<String, List<UploadMoreDataConf>> fileNameConfGroup = new HashMap<>(1);
        // 读取配置信息 TODO 从数据库读取

        boolean isSingleFile  = Objects.equals(uploadMoreDataMain.getMainUploadType(), "1");
        if(isSingleFile){
            fileNameConfGroup.put(SINGLE_FILE_KEY, moreDataConfList);
        }else{
            fileNameConfGroup.putAll(moreDataConfList.stream().collect(Collectors.groupingBy(UploadMoreDataConf::getConfFileName)));
        }
        Date tmpUploadDate = getCurUploadDate(uploadMoreDataMain.getDateConfType(), uploadMoreDataMain.getCurUploadDate());

        // 异步执行数据上传
        asyncExecuteHandleUploadData(multipartFileMap, fileNameConfGroup,  tmpUploadDate, isSingleFile);

        return Result.ok("后台处理中 后续结果会通过系统消息通知您");
    }

    /**
     * 转存 解压 上传文件
     */
    private List<UploadDataFileDTO> handleUploadDataFileToDto(boolean isZip, MultipartFile multipartFile, String fileName){
        String fileSuffixType = FileTypeEnum.getFileSuffixType(fileName);
        String tmpFileName = UUID.randomUUID().toString();
        String fileUrl = OssUtils.getStaticPath() + OssUtils.getUploadFileUrl("", tmpFileName, fileSuffixType);
        File tmpFile = new File(fileUrl);
        try {
            multipartFile.transferTo(tmpFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // to UploadDataFileDTO
        List<UploadDataFileDTO> multipartFileMap = new ArrayList<>();
        if(isZip){
            try {
                BatchDecomUtil.deCompress(tmpFile, tmpFile.getParent() , true);
            } catch (IOException e) {
                e.printStackTrace();
            }
            File deCompressDir = new File(tmpFile.getParent() + File.separator + tmpFileName);
            File[] tmpZipFiles = deCompressDir.listFiles();
            if (ObjectUtils.isNotEmpty(tmpZipFiles)){
                Arrays.stream(tmpZipFiles).forEach(innerFile-> {
                    UploadDataFileDTO uploadDataFileDTO = new UploadDataFileDTO();
                    uploadDataFileDTO.setFileName(getFileName(innerFile.getName()));
                    uploadDataFileDTO.setFile(innerFile);
                    uploadDataFileDTO.setFilePath(innerFile.getPath());
                    uploadDataFileDTO.setParentPath(innerFile.getParent());
                    multipartFileMap.add(uploadDataFileDTO);
                });
            }else{
                logger.error("压缩包解压失败！无有效文件");
            }
        }else{
            UploadDataFileDTO uploadDataFileDTO = new UploadDataFileDTO();
            uploadDataFileDTO.setFileName(getFileName(tmpFile.getName()));
            uploadDataFileDTO.setFile(tmpFile);
            uploadDataFileDTO.setFilePath(tmpFile.getPath());
            multipartFileMap.add(uploadDataFileDTO);
        }
        return multipartFileMap;
    }

    /**
     * 异步执行数据上传
     * @param multipartFileMap 文件
     * @param fileNameGroup 配置
     * @param tmpUploadDate 时间
     * @param isSingleFile 是否压缩文件
     */
    private void asyncExecuteHandleUploadData(List<UploadDataFileDTO> multipartFileMap, Map<String, List<UploadMoreDataConf>> fileNameGroup,
                                              Date tmpUploadDate, boolean isSingleFile){
        // 异步执行
        ThreadPoolExecutor threadPool = ThreadPoolUtils.getThreadPool();
        threadPool.execute(()->{
            Date startDate = new Date(System.currentTimeMillis());
            logger.info("开始写入时间：{}", simpleFormat.format(startDate));
            ProSmsVO proSmsVO = new ProSmsVO();
            proSmsVO.setEsTitle("数据上传结果");
            proSmsVO.setEsContent("<h1>${type}</h1><p>${context}</p>");
            proSmsVO.setEsReceiver("admin");
            proSmsVO.setEsType("system");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 1、根据配置 去读取数据
                BatchReaderExcelUtil batchReaderExcelUtil = new BatchReaderExcelUtil();
                List<String[]> allStorageStrategy = new ArrayList<>();
                if(isSingleFile){
                    List<String[]> tmpStorageStrategy = handleUploadConfFileData(batchReaderExcelUtil, fileNameGroup.get(SINGLE_FILE_KEY),
                            multipartFileMap.stream().findAny().orElse(new UploadDataFileDTO()), tmpUploadDate);
                    allStorageStrategy.addAll(tmpStorageStrategy);
                }else{
                    for(Map.Entry<String, List<UploadMoreDataConf>> entry : fileNameGroup.entrySet()){
                        UploadDataFileDTO multipartFileDto = multipartFileMap.stream().filter(item->
                                Objects.equals(item.getFileName(), entry.getKey())).findAny().orElse(new UploadDataFileDTO());
                        List<String[]> tmpStorageStrategy  = handleUploadConfFileData(batchReaderExcelUtil, entry.getValue(), multipartFileDto, tmpUploadDate);
                        allStorageStrategy.addAll(tmpStorageStrategy);
                    }
                }

                // 2、调用策略
                handleStorageStrategy(allStorageStrategy, tmpUploadDate);
                // 计算完成时间
                Date endDate = new Date(System.currentTimeMillis());
                logger.info("写入完成时间：{}",simpleFormat.format(endDate));
                //上传成功
                proSmsVO.setEsParam(objectMapper.convertValue(ImmutableMap.of("type", "写入成功", "context", String.format("【%s】数据写入完成，开始时间:%s,结束时间:%s",
                        uploadMoreDataMain.getMainTemplateName(), simpleFormat.format(startDate),simpleFormat.format(endDate))), String.class));
            }catch (RuntimeException e){
                logger.error("上传数据写入失败，错误信息 {}", e.getMessage());
                proSmsVO.setEsParam(objectMapper.convertValue((ImmutableMap.of("type", "写入失败", "context", ObjectUtils.isNotEmpty(e.getMessage())?e.getMessage():"")),String.class));
            }catch (Exception e){
                logger.error("上传数据读取失败，错误信息 {}", e.getMessage());
                proSmsVO.setEsParam(objectMapper.convertValue(ImmutableMap.of("type", "读取失败", "context", ObjectUtils.isNotEmpty(e.getMessage())?e.getMessage():""),String.class));
            } finally {
                strategySms.sendSms(proSmsVO, new SysUser(), 0L);
                multipartFileMap.stream().filter(item-> item.getFile().exists()).forEach(item->{
                    boolean delete = item.getFile().delete();
                    if(!delete){
                        logger.warn("上传数据临时文件删除失败!->{}", item.getFileName());
                    }
                });
            }
        });
    }


    /***
     * 根据配置 解析文件数据 写入临时表
     */
    private List<String[]> handleUploadConfFileData(BatchReaderExcelUtil batchReaderExcelUtil, List<UploadMoreDataConf> moreDataConfList,
                                                    UploadDataFileDTO multipartFileDto, Date tmpUploadDate) throws Exception{
        // 解析计算数据位置信息
        List<UploadDataExcelDTO> uploadDataExcelDTOS = resolutionUploadExcelConfig(moreDataConfList, tmpUploadDate);
        // 读取文件数据
        HandleExcelRowData handleExcelRowData = new HandleExcelRowData(uploadDataExcelDTOS);
        // 注册监听
        ReaderDataListenerImpl readerDataListener = new ReaderDataListenerImpl(sqlSessionFactory, desFormDataBatchUploadMapper);
        handleExcelRowData.registerListener(readerDataListener);
        batchReaderExcelUtil.readExcel(multipartFileDto.getFile(), handleExcelRowData);
        // 触发写入操作
        handleExcelRowData.insertToExcelRowData();
        // 获取数据写入策略
        return readerDataListener.getStorageStrategy();
    }


    /**
     * 处理数据存储策略
     * @param allStorageStrategy 策略
     */
    private void handleStorageStrategy(List<String[]> allStorageStrategy, Date tmpUploadDate) {
        // 根据策略执行不同操作
        // 根据上传类型 找到对应的上传策略
        List<String> delTableName = new ArrayList<>();
        try {
            for (String[] storageStrategyConf : allStorageStrategy) {
                PutStorageStrategy putStorageStrategy = null;
                String putStorageStrategyType = storageStrategyConf[2];
                delTableName.add(storageStrategyConf[1]);
                switch (putStorageStrategyType) {
                    case "1":
                        putStorageStrategy = putStorageStrategyMap.get("fullAmountCover");
                        break;
                    case "2":
                        putStorageStrategy = putStorageStrategyMap.get("fullAmountUpload");
                        break;
                    case "3":
                        putStorageStrategy = putStorageStrategyMap.get("increaseAmountUpload");
                        break;
                    case "4":
                        putStorageStrategy = putStorageStrategyMap.get("increaseAmountUpdate");
                        break;
                    case "5":
                        // 先删除上次上传数据
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(storageStrategyConf[3]);
                        delOlderTableData(storageStrategyConf[0], simpleDateFormat.format(tmpUploadDate));
                        putStorageStrategy = putStorageStrategyMap.get("fullAmountUpload");
                        break;
                }
                if (putStorageStrategy != null) {
                    putStorageStrategy.putStorageStrategy(storageStrategyConf[1], storageStrategyConf[0], desFormDataBatchUploadMapper);
                    desFormDataBatchUploadMapper.dropTable(storageStrategyConf[1]);
                }
            }
        } catch (Exception e) {
            logger.error("执行最后保存逻辑出错 错误信息:{}", e.getMessage());
            throw new RuntimeException(String.format("执行最后保存逻辑出错 错误信息:%s", e.getMessage()));
        }finally {
            try {
                delTableName.forEach(tmptableName->{
                    if(desFormDataBatchUploadMapper.findTable(tmptableName) > 0){
                        desFormDataBatchUploadMapper.dropTable(tmptableName);
                    }
                });
            }catch (Exception e){
                logger.warn("删除临时表失败 信息: {}", e.getMessage());
            }
        }
    }

    /**
     * 删除 当天上次上传的数据
     */
    private void delOlderTableData(String tableName, String uploadDate){
        if(desFormDataBatchUploadMapper.queryListDataSql(tableName, uploadDate) > 0){
            desFormDataBatchUploadMapper.dropTable(tableName);
        }
    }


    /**
     * 处理校验文件导入的规则
     * @param uploadMoreDataConfList 文件配置
     * @param curUploadDate 当前时间
     * @return 配置信息
     */
    private List<UploadDataExcelDTO> resolutionUploadExcelConfig(List<UploadMoreDataConf> uploadMoreDataConfList, Date curUploadDate){
        // 根据所属sheet下标分组
        return uploadMoreDataConfList.stream().peek(item->{
            // x轴开始位置 下标
            Integer confSheetXoffsetIndex = calcConfSheetXoffsetNum(item.getConfSheetXoffset());
            item.setConfSheetXoffsetIndex(confSheetXoffsetIndex);
            // x轴结束位置 下标
            Integer confSheetXendOffsetIndex = confSheetXoffsetIndex + item.getConfSheetFieldNumber();
            item.setConfSheetXendOffsetIndex(confSheetXendOffsetIndex);
            // y轴开始位置 下标
            Integer confSheetYoffsetIndex = item.getConfSheetYoffset() - 1;
            item.setConfSheetYoffsetIndex(confSheetYoffsetIndex);
            // y轴结束位置 下标
            Integer confSheetYendOffsetIndex = calcConfSheetYoffsetNum(item.getConfSheetRowType(), confSheetYoffsetIndex, item.getConfSheetRowNumber());
            item.setConfSheetYendOffsetIndex(confSheetYendOffsetIndex);
        }).map(itemUploadMoreDataConf->{
            UploadDataExcelDTO uploadDataExcel = new UploadDataExcelDTO();
            uploadDataExcel.setConfSheetIndex(itemUploadMoreDataConf.getConfSheetIndex() - 1);
            uploadDataExcel.setConfSheetXoffsetIndex(itemUploadMoreDataConf.getConfSheetXoffsetIndex());
            uploadDataExcel.setConfSheetXendOffsetIndex(itemUploadMoreDataConf.getConfSheetXendOffsetIndex());
            uploadDataExcel.setConfSheetYoffsetIndex(itemUploadMoreDataConf.getConfSheetYoffsetIndex());
            uploadDataExcel.setConfSheetYendOffsetIndex(itemUploadMoreDataConf.getConfSheetYendOffsetIndex());
            UploadMoreDataConfDTO uploadMoreDataConfDTO = new UploadMoreDataConfDTO();
            BeanUtils.copyProperties(itemUploadMoreDataConf, uploadMoreDataConfDTO);
            uploadDataExcel.setUploadMoreDataConf(uploadMoreDataConfDTO);
            // 校验数据源是否存在
            boolean isExist = false;
            String tableName = itemUploadMoreDataConf.getConfTargetDatasource().toUpperCase();

            List<DesTableColumnDTO> tableColumns;
            if(tableName.contains("\\.")){
                String[] tableNameSplit = tableName.split("\\.");
                tableColumns = desFormDataBatchUploadMapper.getAllColumns(tableNameSplit[0], tableNameSplit[1]);
            }else{
                tableColumns = desFormDataBatchUploadMapper.getColumns(tableName);
            }
            uploadDataExcel.setTableColumns(tableColumns);

            if(tableColumns != null && tableColumns.size() > 0){
                isExist = true;
                // 校验字段 过滤不存在的字段
                List<String> tableFields = tableColumns.stream().map(item -> String.valueOf(item.getColumnName())).collect(Collectors.toList());
                List<String> oldTableFields = itemUploadMoreDataConf.getTableFields();
                if(oldTableFields != null){
                    uploadDataExcel.setTitleData(oldTableFields.stream().map(tmpField -> {
                        if(tableFields.contains(tmpField.toUpperCase())){
                            return tmpField.toUpperCase();
                        }
                        logger.warn("导入表{} 字段不存在跳过! 字段: {}", tableName, tmpField);
                        return "";
                    }).collect(Collectors.toList()));
                }else{
                    uploadDataExcel.setTitleData(tableFields);
                }
                // 转换时间格式
                if(ObjectUtils.isNotEmpty(itemUploadMoreDataConf.getTableDateFieldFormat())){
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(itemUploadMoreDataConf.getTableDateFieldFormat());
                    uploadDataExcel.setTableDateFieldFormatVal(simpleDateFormat.format(curUploadDate));
                }
            }
            uploadDataExcel.setTableUploadDateField(itemUploadMoreDataConf.getTableUploadDateField());
            uploadDataExcel.setTableIsExist(isExist);
            return uploadDataExcel;
        }).collect(Collectors.toList());
    }

    /**
     * 计算x轴下标
     * @param confSheetXoffset x轴 excl 内的字母 如 A = 0 AA =
     */
    private static Integer calcConfSheetXoffsetNum(String confSheetXoffset){
        char[] xchars = confSheetXoffset.toUpperCase().toCharArray();
        int confSheetXoffsetNum = 0;
        for (int i = xchars.length - 1, j = 1; i >= 0; i--, j *= 26) {
            char c = xchars[i];
            if (c < 'A' || c > 'Z') {
                return 0;
            }
            confSheetXoffsetNum += ((int) c - 64) * j;
        }
        return --confSheetXoffsetNum;
    }

    /**
     * 计算 y轴结束位置下标
     * @param confSheetRowType 读取行类型
     * @param confSheetYoffsetIndex y下标
     * @param confSheetRowNumber 行数
     * @return Integer
     */
    private static Integer calcConfSheetYoffsetNum(String confSheetRowType, Integer confSheetYoffsetIndex, Integer confSheetRowNumber){
        int confSheetYendOffsetIndex;
        if ("1".equals(confSheetRowType)) {
            confSheetYendOffsetIndex = confSheetYoffsetIndex + confSheetRowNumber - 1;
        } else {
            confSheetYendOffsetIndex = -1;
        }
        return confSheetYendOffsetIndex;
    }


    /**
     * 获取文件名 不带后缀
     * @param filename 文件名
     */
    private static String getFileName(String filename){
        return filename.substring(0,filename.lastIndexOf("."));
    }

    /**
     * 转换上传数据选择时间的时间 为 date
     * @param dateConfType 时间配置类型
     * @param curUploadDate 当前时间
     */
    private Date getCurUploadDate(Integer dateConfType, String curUploadDate)  {
        // 设置了数据上传时间
        if(Objects.equals(dateConfType,1)){
            try {
                Date tmpCurUploadDate = new SimpleDateFormat("yyyy-MM-dd").parse(curUploadDate);
                Calendar nowtime = new GregorianCalendar();
                return calcDate(tmpCurUploadDate, nowtime.get(Calendar.SECOND), nowtime.get(Calendar.MINUTE), nowtime.get(Calendar.HOUR_OF_DAY));
            }catch (ParseException e){
                throw new RuntimeException("时间格式不对！yyyy-MM-dd");
            }
        }
        return new Date(System.currentTimeMillis());
    }

    /**
     * 设置 时分秒
     * @param date 时间
     * @param second 秒
     * @param minute 分
     * @param hour 时
     */
    private static Date calcDate(Date date, int second, int minute, int hour){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.SECOND, second);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        return calendar.getTime();
    }
}
