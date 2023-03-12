package com.yanhuanxy.multifunexport.demo.designpattern.singleton;

/**
 * 单例模式
 * 1、饿汉式
 *
 */
public class SingletonDemo {

    public static void main(String[] args) {
        // 1-1 饿汉式
        SingletonDemoHungry hungry1 = SingletonDemoHungry.getInstance();
        SingletonDemoHungry hungry2 = SingletonDemoHungry.getInstance();
        System.out.println(hungry1 = hungry2);
        // 1-2 饿汉式 静态代码块创建
        SingletonDemoHungry2 hungry2_1 = SingletonDemoHungry2.getInstance();
        SingletonDemoHungry2 hungry2_2 = SingletonDemoHungry2.getInstance();
        System.out.println(hungry2_1 = hungry2_2);
        // 2 懒汉式
        SingletonDemoLazy lazy1 = SingletonDemoLazy.getInstance();
        SingletonDemoLazy lazy2 = SingletonDemoLazy.getInstance();
        System.out.println(lazy1 = lazy2);
        // 3 静态内部类
        SingletonDemoStatic static1 = SingletonDemoStatic.getInstance();
        SingletonDemoStatic static2 = SingletonDemoStatic.getInstance();
        System.out.println(static1 = static2);
        // 4 枚举 严格来讲 语法上讲最完美
        SingletonDemoEnum enum1 = SingletonDemoEnum.INSTANCE;
        SingletonDemoEnum enum2 = SingletonDemoEnum.INSTANCE;
        System.out.println(enum1 = enum2);
    }

}
