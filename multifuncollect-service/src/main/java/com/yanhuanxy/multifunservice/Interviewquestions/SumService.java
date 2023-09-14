package com.yanhuanxy.multifunservice.Interviewquestions;

import java.util.Vector;

public class SumService {
    protected volatile Vector<Long> tmpValues = new Vector<>();

    static class AService{
        public Long random(){
            return Double.valueOf(Math.random() * 10).longValue();
        }
    }

    static class BService{
        public Long random(){
            return Double.valueOf(Math.random() * 20).longValue();
        }
    }

    static class CService{
        public Long random(){
            return Double.valueOf(Math.random() * 30).longValue();
        }
    }

    public Long sum(String name){
        ThreadGroup threadGroup = new ThreadGroup("线程组"+ name);
        Thread threadA = new Thread(threadGroup,() -> {
            Long randomA = new AService().random();
            boolean add = tmpValues.add(randomA);
        }, "AService");
        Thread threadB = new Thread(threadGroup,() ->{
            Long randomB = new BService().random();
            boolean add = tmpValues.add(randomB);
        }, "BService");
        Thread threadC = new Thread(threadGroup,() ->{
            Long randomC = new CService().random();
            boolean add = tmpValues.add(randomC);
        }, "CService");
        threadA.start();
        threadB.start();
        threadC.start();

        while (true){
            boolean theadIsSuccess = threadGroup.activeCount() == 0;
            if(theadIsSuccess){
                break;
            }
        }

        return this.tmpValues.stream().mapToLong(item ->{
            System.out.println("item->"+ item);
            return item;
        } ).sum();
    }

    public static void main(String[] args) {
        SumService sumService = new SumService();
        String name = Thread.currentThread().getName();
        Long sum = sumService.sum(name);
        System.out.println(sum);
    }
}
