package com.yanhuanxy.multifunexport.demo.designpattern.singleton;

/**
 * 单例 懒汉式
 * 用到时候才初始化 多线程访问时 获取到的对象可能不一致  线程不安全
 * synchronized  加锁  缺点-> 效率将会降低
 */
public class SingletonDemoLazy3 {
    private static SingletonDemoLazy3 INSTANCE;

    /**
     * 锁当前对象
     * 减小锁粒度
     */
    public static SingletonDemoLazy3 getInstance(){
        /**
         * 减小同步代码块的方式来提高效率-> 不过不可行 多线程还是会导致 对象不一致
         */
        if(INSTANCE == null){
            synchronized(SingletonDemoLazy3.class){
                INSTANCE = new SingletonDemoLazy3();
            }
        }
        return INSTANCE;
    }

    private SingletonDemoLazy3(){
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
                SingletonDemoLazy3 tmpLazy = SingletonDemoLazy3.getInstance();
                // 同一个类的hashcode 一定不一样
                System.out.println(tmpLazy.hashCode());
            }).start();
        }
    }
}
