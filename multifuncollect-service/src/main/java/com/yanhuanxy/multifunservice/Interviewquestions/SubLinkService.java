package com.yanhuanxy.multifunservice.Interviewquestions;


import java.util.LinkedList;
import java.util.Objects;
import java.util.stream.Collectors;

public class SubLinkService {

    public LinkedList<Integer> subLinked(LinkedList<Integer> linked1, LinkedList<Integer> linked2){
        // 校验链表1
        checkLinked(linked1);
        // 校验链表2
        checkLinked(linked2);

        String linked1Val = linked1.stream().map(Object::toString).collect(Collectors.joining());
        String linked2Val = linked2.stream().map(Object::toString).collect(Collectors.joining());
        // 校验链表1
        checkLinked(linked1.size(), linked1Val.length());
        // 校验链表2
        checkLinked(linked2.size(), linked2Val.length());

        // 校验值
        long linked1L = Long.parseLong(linked1Val);
        long linked2L = Long.parseLong(linked2Val);
        if(linked1L < linked2L){
            throw new RuntimeException("不符合规则");
        }
        long resultL = linked1L - linked2L;
        LinkedList<Integer> result = new LinkedList<>();
        String resultVal = String.valueOf(resultL);
        char[] resultC = resultVal.toCharArray();
        for (char tmp : resultC) {
            result.add(Integer.parseInt(String.valueOf(tmp)));
        }
        return result;
    }

    private void checkLinked(LinkedList<Integer> linked){
        if(Objects.isNull(linked) || linked.size() == 0){
            throw new RuntimeException("数组为空");
        }

        Integer first = linked.getFirst();
        if(first == 0 && linked.size() > 1){
            throw new RuntimeException("不符合规则");
        }
    }

    private void checkLinked(Integer linkedSize, Integer linkedLength){
        if(!linkedSize.equals(linkedLength)){
            throw new RuntimeException("不符合规则");
        }
    }

    public static void main(String[] args) {
        SubLinkService sumService = new SubLinkService();
        LinkedList<Integer> linkedList1 = new LinkedList();
        linkedList1.add(1);
        linkedList1.add(9);
        linkedList1.add(1);
        LinkedList<Integer> linkedList2 = new LinkedList();
        linkedList2.add(1);
        LinkedList<Integer> subLinked = sumService.subLinked(linkedList1, linkedList2);
        System.out.println(subLinked.stream().map(Objects::toString).collect(Collectors.joining()));
    }
}
