package com.yanhuanxy.webflux;


import reactor.core.publisher.Flux;
import reactor.util.function.Tuple2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;
import java.util.concurrent.TimeUnit;

public class demo {

    private void steamFlux() throws InterruptedException{
        //        Flux<Tuple2<Long,Long>> flux = Flux.interval(Duration.ofMillis(300)).take(5)
//                .<Long>handle((item, sink) -> {
//            if(item > 3) {
//                System.out.println(item);
//                sink.error(new RuntimeException("1111"));
//                return;
//            }
//                    sink.next(item);
//        }).retry(3).elapsed();
//        flux.subscribe((item)->{
//            System.out.println("A");
//            System.out.println(item);
//        }, (item)->{
//            System.out.println("E");
//            System.out.println(item.getMessage());
//        });
        Flux.generate(()-> 0, (i,sink)->{
            sink.next("2 * " +i + " = "+ 2*i);
            if(i==9){
                sink.complete();
            }
            return i+1;
        }).replay(2).take(10).elapsed().subscribe();

        // 发布者
        try(SubmissionPublisher<String> publisher = new SubmissionPublisher<>()) {
            // 订阅者
            Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    // 订阅者 处理 订阅请求
                    subscription.request(Integer.MAX_VALUE);
                }

                @Override
                public void onNext(String item) {
                    System.out.println("订阅者处理发布者消息\r\n" + item);
                }

                @Override
                public void onError(Throwable throwable) {
                    System.out.println("订阅者处理发布者异常消息\r\n" + throwable);
                }

                @Override
                public void onComplete() {
                    System.out.println("订阅者处理发布者的完成消息");
                }
            };
            // 处理器
            Flow.Processor<String, String> processor = new Flow.Processor<String,String>(){
                private Flow.Subscriber<? super String> subscriber;

                @Override
                public void subscribe(Flow.Subscriber<? super String> subscriber) {
                    this.subscriber = subscriber;
                }

                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    subscriber.onSubscribe(subscription);
                }

                @Override
                public void onNext(String item) {
                    System.out.println("processor----\r\n" + item);

                    subscriber.onNext(item + "(processed)");
                }

                @Override
                public void onError(Throwable throwable) {
                    subscriber.onError(throwable);
                    System.out.println("processor----\r\n" + throwable.getMessage());
                }

                @Override
                public void onComplete() {
                    subscriber.onComplete();
                    System.out.println("processor----\r\n" + "onComplete");
                }
            };
            // 发布者 跟 订阅者 建立关系
            // 处理器 关联 订阅者
            processor.subscribe(subscriber);
            // 发布者 关联 处理器
            publisher.subscribe(processor);

            publisher.submit("111");

        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        CountDownLatch latch = new CountDownLatch(1);
        boolean await = latch.await(5, TimeUnit.SECONDS);
    }

    public static void main(String[] args) throws InterruptedException {
        convert();

        Flux.just("xww xas")
                .map(item-> item.split(" ")).map(item-> {
                    List<String> a = new ArrayList<>();
                    for (String s : item) {
                        a.addAll(Arrays.asList(s.split("")));
                    }
                    return a;
                }).flatMap(item-> Flux.fromArray(item.toArray())).subscribe(System.out::println);

    }

    private static void convert(){

    }
}
