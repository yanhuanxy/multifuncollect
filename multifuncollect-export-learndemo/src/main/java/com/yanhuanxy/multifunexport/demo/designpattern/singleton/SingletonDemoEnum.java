package com.yanhuanxy.multifunexport.demo.designpattern.singleton;

/**
 * 单例 枚举式
 * 保证下线程同步 还可以防止反序列化
 *
 */
public enum SingletonDemoEnum {
    INSTANCE;

    private SingletonDemoEnum(){ }

    /**
     * 名称
     */
    private String name;

    public String getName() {
        return name;
    }

    public static void main(String[] args) {
        for (int i = 0; i < 100; i++) {
            new Thread(()->{
                // 同一个类的hashcode 一定不一样
                System.out.println(SingletonDemoEnum.INSTANCE.hashCode());
            }).start();
        }
    }
}
