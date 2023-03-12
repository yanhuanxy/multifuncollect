package com.yanhuanxy.multifunexport.demo.designpattern.singleton;

/**
 * 单例 懒汉式
 * 用到时候才初始化 多线程访问时 获取到的对象可能不一致  线程不安全
 * synchronized  加锁  缺点-> 效率将会降低
 */
public class SingletonDemoLazy2 {
    private static SingletonDemoLazy2 INSTANCE;

    /**
     * 锁当前对象
     */
    public static synchronized SingletonDemoLazy2 getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SingletonDemoLazy2();
        }
        return INSTANCE;
    }

    private SingletonDemoLazy2(){
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
                SingletonDemoLazy2 tmpLazy = SingletonDemoLazy2.getInstance();
                // 同一个类的hashcode 一定不一样
                System.out.println(tmpLazy.hashCode());
            }).start();
        }
    }
}
