package com.yanhuanxy.multifunexport.demo.designpattern.singleton;

/**
 * 单例 静态内部类
 * JVM 保证单利
 * 加载外部类时不加载内部类  -> 实现懒加载
 *
 */
public class SingletonDemoStatic {

    private SingletonDemoStatic(){
        super();
    }

    /**
     * 内部类创建
     */
    private static class SingletonDemoStaticHolder{
        private final static SingletonDemoStatic INSTANCE = new SingletonDemoStatic();
    }

    /**
     * 锁SingletonDemoLazy4类
     * 减小锁粒度
     */
    public static SingletonDemoStatic getInstance(){

        return SingletonDemoStaticHolder.INSTANCE;
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
                SingletonDemoStatic tmpLazy = SingletonDemoStatic.getInstance();
                // 同一个类的hashcode 一定不一样
                System.out.println(tmpLazy.hashCode());
            }).start();
        }
    }
}
