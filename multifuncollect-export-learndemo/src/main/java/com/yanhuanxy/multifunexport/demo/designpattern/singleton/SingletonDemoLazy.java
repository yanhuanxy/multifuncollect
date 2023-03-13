package com.yanhuanxy.multifunexport.demo.designpattern.singleton;

/**
 * 单例 懒汉式
 * 用到时候才初始化 多线程访问时 获取到的对象可能不一致  线程不安全
 */
public class SingletonDemoLazy {
    private static SingletonDemoLazy INSTANCE;

    public static SingletonDemoLazy getInstance(){
        if(INSTANCE == null){
            INSTANCE = new SingletonDemoLazy();
        }
        return INSTANCE;
    }

    private SingletonDemoLazy(){
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
                SingletonDemoLazy tmpLazy = SingletonDemoLazy.getInstance();
                // 同一个类的hashcode 一定不一样
                System.out.println(tmpLazy.hashCode());
            }).start();
        }
    }
}
