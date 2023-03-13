package com.yanhuanxy.multifunexport.demo.designpattern.singleton;

/**
 * 单例 懒汉式
 * 用到时候才初始化
 * synchronized  加锁
 *
 */
public class SingletonDemoLazy4 {
    /**
     * valatile 编译时 可能会导致指令重排 JIT
     */
    private static volatile SingletonDemoLazy4 INSTANCE;

    /**
     * 锁SingletonDemoLazy4类
     * 减小锁粒度
     */
    public static SingletonDemoLazy4 getInstance(){
        /**
         * double check look
         * 双重检查
         */
        if(INSTANCE == null){
            synchronized(SingletonDemoLazy4.class){
                if(INSTANCE == null){
                    INSTANCE = new SingletonDemoLazy4();
                }
            }
        }
        return INSTANCE;
    }

    private SingletonDemoLazy4(){
        super();
    }

    /**
     * 名称
     */
    private final String name = "大头";

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                SingletonDemoLazy4 tmpLazy = SingletonDemoLazy4.getInstance();
                // 同一个类的hashcode 一定不一样
                System.out.println(tmpLazy.hashCode());
            }).start();
        }
    }
}
