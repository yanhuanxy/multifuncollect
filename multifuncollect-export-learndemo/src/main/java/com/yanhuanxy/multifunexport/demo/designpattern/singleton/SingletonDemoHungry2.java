package com.yanhuanxy.multifunexport.demo.designpattern.singleton;

/**
 * 单例 饿汉式
 * 类加载到内存后， 就实例化一个单例 jvm保证《线程安全！！！》
 * 简单实用 推荐！
 * 缺点-> 不管是否用到与否 类装载时就完成实列化
 */
public class SingletonDemoHungry2 {
    private static final SingletonDemoHungry2 INSTANCE;

    static {
        INSTANCE = new SingletonDemoHungry2();
    }

    public static SingletonDemoHungry2 getInstance(){
        return INSTANCE;
    }

    private SingletonDemoHungry2(){
        super();
    }

    /**
     * 名称
     */
    private final String name = "大头";

    public String getName() {
        return name;
    }
}
