package com.yanhuanxy.multifunservice.Interviewquestions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class BatchService {
    private static final Integer MAX_NUM = 100;

    private static final Integer DEFAULT_NUM = 10;

    static class BService{
        public List<Integer> get(List<Integer> ids){
            if(Objects.isNull(ids)){
                throw new RuntimeException("无效参数");
            }
            int idsSize = ids.size();
            if(idsSize> MAX_NUM){
                throw new RuntimeException("集合值过多");
            }
            int index = 0;
            BatchService.AService aService = new BatchService.AService();
            List<Integer> result = new ArrayList<>();
            while (index * DEFAULT_NUM < idsSize){
                int startIndex = index * DEFAULT_NUM, endIndex = startIndex + DEFAULT_NUM;
                if(endIndex > idsSize){
                    endIndex = idsSize;
                }
                List<Integer> tmpIds = ids.subList(startIndex, endIndex);
                List<Integer> tmpIdsResult = aService.get(tmpIds);
                result.addAll(tmpIdsResult);
                index++;
            }
            return result;
        }
    }


    static class AService{
        public List<Integer> get(List<Integer> ids){
            if(Objects.isNull(ids)){
                throw new RuntimeException("无效参数");
            }
            return new ArrayList<>(ids);
        }
    }


    public static void main(String[] args) {
        List<Integer> input = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            input.add(i);
        }
        BatchService.BService bService = new BatchService.BService();
        List<Integer> result = bService.get(input);
        System.out.println(result.size());
        result.forEach(System.out::println);
    }
}
