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
     * ??????
     */
    private final static String MAIN_UPLOAD_TYPE = "1";

    /**
     * ???????????? Key
     */
    private final static String SINGLE_FILE_KEY = "onlyone";

    /**
     * ?????????
     */
    private UploadMoreDataMain uploadMoreDataMain;

    /**
     * ??????
     */
    private List<UploadMoreDataConf> moreDataConfList;


    public void setUploadMoreDataMain(UploadMoreDataMain uploadMoreDataMain) {
        this.uploadMoreDataMain = uploadMoreDataMain;
    }

    public void setMoreDataConfList(List<UploadMoreDataConf> moreDataConfList) {
        this.moreDataConfList = moreDataConfList;
    }

    /**
     * ?????????????????? -> ????????????
     *           -> ??????????????? -> ?????? -> ????????????
     * ???????????? ????????????
     * ?????? -> ??????????????????????????????  -> ????????????
     *     -> ??????????????????????????????
     * ???????????? -> ???????????? -> max ???????????? -> ????????????
     *        -> ?????? ??????????????? ??????????????????
     *        -> ???????????????
     */

    public Result<?> handUploadFile(MultipartFile multipartFile, Long mainId, String curUploadDate){
        String fileName = multipartFile.getOriginalFilename();
        boolean exist = FileTypeEnum.exist(fileName);
        if(!exist){
            logger.error("?????? {} ???????????????????????????????????????????????????.xls,.xlsx,.csv,.zip???", mainId);
            return Result.error("????????????????????????.xls,.xlsx,.csv,.zip???");
        }
        uploadMoreDataMain.setCurUploadDate(curUploadDate);

        if(ObjectUtils.isEmpty(uploadMoreDataMain.getDateConfType())){
            uploadMoreDataMain.setDateConfType(0);
        }
        String mainUploadType = uploadMoreDataMain.getMainUploadType().trim();
        boolean isZip = FileTypeEnum.isZip(fileName);
        if(Objects.equals("1", mainUploadType) && isZip){
            logger.error("??????{} ?????????????????????????????????????????????????????????????????????.xls,.xlsx,.csv???", mainId);
            return Result.error("?????????????????????????????????.xls,.xlsx,.csv???");
        }else if((Objects.equals("2", mainUploadType)  || Objects.equals("3", mainUploadType) ) && !isZip){
            logger.error("??????{} ?????????????????????????????????????????????????????????????????????.zip???", mainId);
            return Result.error("?????????????????????????????????.zip???");
        }
        // ?????? ?????? ????????????
        List<UploadDataFileDTO> multipartFileMap = handleUploadDataFileToDto(isZip, multipartFile, fileName);
        // ???????????????????????????????????????
        Map<String, List<UploadMoreDataConf>> fileNameConfGroup = new HashMap<>(1);
        // ?????????????????? TODO ??????????????????

        boolean isSingleFile  = Objects.equals(uploadMoreDataMain.getMainUploadType(), "1");
        if(isSingleFile){
            fileNameConfGroup.put(SINGLE_FILE_KEY, moreDataConfList);
        }else{
            fileNameConfGroup.putAll(moreDataConfList.stream().collect(Collectors.groupingBy(UploadMoreDataConf::getConfFileName)));
        }
        Date tmpUploadDate = getCurUploadDate(uploadMoreDataMain.getDateConfType(), uploadMoreDataMain.getCurUploadDate());

        // ????????????????????????
        asyncExecuteHandleUploadData(multipartFileMap, fileNameConfGroup,  tmpUploadDate, isSingleFile);

        return Result.ok("??????????????? ??????????????????????????????????????????");
    }

    /**
     * ?????? ?????? ????????????
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
                logger.error("???????????????????????????????????????");
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
     * ????????????????????????
     * @param multipartFileMap ??????
     * @param fileNameGroup ??????
     * @param tmpUploadDate ??????
     * @param isSingleFile ??????????????????
     */
    private void asyncExecuteHandleUploadData(List<UploadDataFileDTO> multipartFileMap, Map<String, List<UploadMoreDataConf>> fileNameGroup,
                                              Date tmpUploadDate, boolean isSingleFile){
        // ????????????
        ThreadPoolExecutor threadPool = ThreadPoolUtils.getThreadPool();
        threadPool.execute(()->{
            Date startDate = new Date(System.currentTimeMillis());
            logger.info("?????????????????????{}", simpleFormat.format(startDate));
            ProSmsVO proSmsVO = new ProSmsVO();
            proSmsVO.setEsTitle("??????????????????");
            proSmsVO.setEsContent("<h1>${type}</h1><p>${context}</p>");
            proSmsVO.setEsReceiver("admin");
            proSmsVO.setEsType("system");
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                // 1??????????????? ???????????????
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

                // 2???????????????
                handleStorageStrategy(allStorageStrategy, tmpUploadDate);
                // ??????????????????
                Date endDate = new Date(System.currentTimeMillis());
                logger.info("?????????????????????{}",simpleFormat.format(endDate));
                //????????????
                proSmsVO.setEsParam(objectMapper.convertValue(ImmutableMap.of("type", "????????????", "context", String.format("???%s????????????????????????????????????:%s,????????????:%s",
                        uploadMoreDataMain.getMainTemplateName(), simpleFormat.format(startDate),simpleFormat.format(endDate))), String.class));
            }catch (RuntimeException e){
                logger.error("??????????????????????????????????????? {}", e.getMessage());
                proSmsVO.setEsParam(objectMapper.convertValue((ImmutableMap.of("type", "????????????", "context", ObjectUtils.isNotEmpty(e.getMessage())?e.getMessage():"")),String.class));
            }catch (Exception e){
                logger.error("??????????????????????????????????????? {}", e.getMessage());
                proSmsVO.setEsParam(objectMapper.convertValue(ImmutableMap.of("type", "????????????", "context", ObjectUtils.isNotEmpty(e.getMessage())?e.getMessage():""),String.class));
            } finally {
                strategySms.sendSms(proSmsVO, new SysUser(), 0L);
                multipartFileMap.stream().filter(item-> item.getFile().exists()).forEach(item->{
                    boolean delete = item.getFile().delete();
                    if(!delete){
                        logger.warn("????????????????????????????????????!->{}", item.getFileName());
                    }
                });
            }
        });
    }


    /***
     * ???????????? ?????????????????? ???????????????
     */
    private List<String[]> handleUploadConfFileData(BatchReaderExcelUtil batchReaderExcelUtil, List<UploadMoreDataConf> moreDataConfList,
                                                    UploadDataFileDTO multipartFileDto, Date tmpUploadDate) throws Exception{
        // ??????????????????????????????
        List<UploadDataExcelDTO> uploadDataExcelDTOS = resolutionUploadExcelConfig(moreDataConfList, tmpUploadDate);
        // ??????????????????
        HandleExcelRowData handleExcelRowData = new HandleExcelRowData(uploadDataExcelDTOS);
        // ????????????
        ReaderDataListenerImpl readerDataListener = new ReaderDataListenerImpl(sqlSessionFactory, desFormDataBatchUploadMapper);
        handleExcelRowData.registerListener(readerDataListener);
        batchReaderExcelUtil.readExcel(multipartFileDto.getFile(), handleExcelRowData);
        // ??????????????????
        handleExcelRowData.insertToExcelRowData();
        // ????????????????????????
        return readerDataListener.getStorageStrategy();
    }


    /**
     * ????????????????????????
     * @param allStorageStrategy ??????
     */
    private void handleStorageStrategy(List<String[]> allStorageStrategy, Date tmpUploadDate) {
        // ??????????????????????????????
        // ?????????????????? ???????????????????????????
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
                        // ???????????????????????????
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
            logger.error("?????????????????????????????? ????????????:{}", e.getMessage());
            throw new RuntimeException(String.format("?????????????????????????????? ????????????:%s", e.getMessage()));
        }finally {
            try {
                delTableName.forEach(tmptableName->{
                    if(desFormDataBatchUploadMapper.findTable(tmptableName) > 0){
                        desFormDataBatchUploadMapper.dropTable(tmptableName);
                    }
                });
            }catch (Exception e){
                logger.warn("????????????????????? ??????: {}", e.getMessage());
            }
        }
    }

    /**
     * ?????? ???????????????????????????
     */
    private void delOlderTableData(String tableName, String uploadDate){
        if(desFormDataBatchUploadMapper.queryListDataSql(tableName, uploadDate) > 0){
            desFormDataBatchUploadMapper.dropTable(tableName);
        }
    }


    /**
     * ?????????????????????????????????
     * @param uploadMoreDataConfList ????????????
     * @param curUploadDate ????????????
     * @return ????????????
     */
    private List<UploadDataExcelDTO> resolutionUploadExcelConfig(List<UploadMoreDataConf> uploadMoreDataConfList, Date curUploadDate){
        // ????????????sheet????????????
        return uploadMoreDataConfList.stream().peek(item->{
            // x??????????????? ??????
            Integer confSheetXoffsetIndex = calcConfSheetXoffsetNum(item.getConfSheetXoffset());
            item.setConfSheetXoffsetIndex(confSheetXoffsetIndex);
            // x??????????????? ??????
            Integer confSheetXendOffsetIndex = confSheetXoffsetIndex + item.getConfSheetFieldNumber();
            item.setConfSheetXendOffsetIndex(confSheetXendOffsetIndex);
            // y??????????????? ??????
            Integer confSheetYoffsetIndex = item.getConfSheetYoffset() - 1;
            item.setConfSheetYoffsetIndex(confSheetYoffsetIndex);
            // y??????????????? ??????
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
            // ???????????????????????????
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
                // ???????????? ????????????????????????
                List<String> tableFields = tableColumns.stream().map(item -> String.valueOf(item.getColumnName())).collect(Collectors.toList());
                List<String> oldTableFields = itemUploadMoreDataConf.getTableFields();
                if(oldTableFields != null){
                    uploadDataExcel.setTitleData(oldTableFields.stream().map(tmpField -> {
                        if(tableFields.contains(tmpField.toUpperCase())){
                            return tmpField.toUpperCase();
                        }
                        logger.warn("?????????{} ?????????????????????! ??????: {}", tableName, tmpField);
                        return "";
                    }).collect(Collectors.toList()));
                }else{
                    uploadDataExcel.setTitleData(tableFields);
                }
                // ??????????????????
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
     * ??????x?????????
     * @param confSheetXoffset x??? excl ???????????? ??? A = 0 AA =
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
     * ?????? y?????????????????????
     * @param confSheetRowType ???????????????
     * @param confSheetYoffsetIndex y??????
     * @param confSheetRowNumber ??????
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
     * ??????????????? ????????????
     * @param filename ?????????
     */
    private static String getFileName(String filename){
        return filename.substring(0,filename.lastIndexOf("."));
    }

    /**
     * ??????????????????????????????????????? ??? date
     * @param dateConfType ??????????????????
     * @param curUploadDate ????????????
     */
    private Date getCurUploadDate(Integer dateConfType, String curUploadDate)  {
        // ???????????????????????????
        if(Objects.equals(dateConfType,1)){
            try {
                Date tmpCurUploadDate = new SimpleDateFormat("yyyy-MM-dd").parse(curUploadDate);
                Calendar nowtime = new GregorianCalendar();
                return calcDate(tmpCurUploadDate, nowtime.get(Calendar.SECOND), nowtime.get(Calendar.MINUTE), nowtime.get(Calendar.HOUR_OF_DAY));
            }catch (ParseException e){
                throw new RuntimeException("?????????????????????yyyy-MM-dd");
            }
        }
        return new Date(System.currentTimeMillis());
    }

    /**
     * ?????? ?????????
     * @param date ??????
     * @param second ???
     * @param minute ???
     * @param hour ???
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
