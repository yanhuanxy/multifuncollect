package com.yanhuanxy.multifunservice.theaddemo;

/**
 * 进程 线程 协程/纤程
 * 一个程序中不同的路径即为线程
 *
 * 启动线程的三种方式
 * 1、继承 Thread
 * 2、实现 Runnable
 * 3、线程池 Executors.newCachedThread 获取线程 或  lambda 表达式实现线程
 *
 * cpu 没有线程的概念 只有一条一条的指令执行
 *
 * sleep  线程进入等待状态
 *
 * yield  运行状态 让出线程一短时间 再执行 由 Running --进入--> Ready状态
 *
 * join   等待指定线程执行完再执行当前线程
 *
 * wait   释放锁 不再继续向下执行
 *
 * notify 让调用 wait方法的线程释放锁 通知其他正在等待得线程得到锁
 *
 * notifyAll
 *
 * 线程状态 -> new 、 runnable(Ready、 Running) 、 TimedWaiting 、 Waiting 、 Blocked(阻塞)、 Teminated
 *
 * 关闭线程 表线程正常结束 不建议使用 stop
 *
 * interrupt 可以用作唤醒 sleep超长时间的睡眠线程
 *
 * synchronized 可重入锁  父子类继承 this、super 指锁的对象还是本身  (程序出现异常锁会被释放)
 *
 * synchronized(Object); Object 不能是 String常量 (可使用 字符串实例 即 final new String('字符串'))、
 *  Integer (避免自动装箱 可使用指定唯一索引 即 int a= 0; final new Integer(a))、 Long、 Boolean
 *
 * synchronized 底层实现
 * JDK早期 重量级 需要找操作系统生成锁
 * 后来改进
 *   锁升级概念->
 *   markword 只记录线程ID (偏向锁)
 *   如果线程争用 升级为 (自旋锁)
 *   默认自旋10次 超过 升级为 (重量级锁)
 *
 * 1、加锁执行时间少 线程少 自旋锁
 * 2、加锁执行时间特别长 线程多 系统锁
 */

public class Demo1 {

    static class TmpThead1 extends Thread{
        @Override
        public void run(){
            System.out.println("线程1->"+ this.getName()+"status"+ this.getState());
        }
    }

    static class TmpThead2 implements Runnable{

        @Override
        public void run() {
            System.out.println("线程2->"+ Thread.currentThread().getName());
        }
    }

    public static void main1(String[] args) {
        new TmpThead1().start();
        new Thread(new TmpThead2()).start();
        new Thread(()->{
            System.out.println("线程3->"+ Thread.currentThread().getName());
        }).start();
    }

    public static void main2(String[] args) {
        new Thread(()->{
            System.out.println("线程3->"+ Thread.currentThread().getName() +"进行休息");
            Thread.yield();
            System.out.println("线程3->"+ Thread.currentThread().getName() + "休息完毕");
        }).start();
    }

    public static void main3(String[] args) {

       Thread thread = new Thread(()->{
            System.out.println("线程3.1->"+ Thread.currentThread().getName() +"进行休息");
           try {
               Thread.sleep(1000000000);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
            System.out.println("线程3.1->"+ Thread.currentThread().getName() + "休息完毕");
        });

        Thread thread2 = new Thread(()->{
            try {
                System.out.println("线程3.2->"+ Thread.currentThread().getName() +"开始等待");
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程3.2->"+ Thread.currentThread().getName() + "等待完毕");
        });
        thread.start();
        thread2.start();
        Thread thread5 = new Thread(()->{
            try {
                System.out.println("线程5.2->"+ Thread.currentThread().getName() +"开始等待");
                Thread.sleep(100000);
                thread.interrupt();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程5.2->"+ Thread.currentThread().getName() + "等待完毕");
        });
        thread5.start();
    }

    static class TmpThead4 implements Runnable{
        private /*volatile*/ int a = 100;

        public int getA() {
            return a;
        }

        @Override
        public /*synchronized*/ void run() {
            a--;
            System.out.println("线程2->"+ Thread.currentThread().getName()+ ">>"+ a);
        }

        public void main(String[] args) {
            TmpThead4 tmpThead4 = new TmpThead4();
            for (int i = 0; i < 50; i++) {
                new Thread(tmpThead4, "1111-"+i).start();
            }
            System.out.println("线程3.2->"+ Thread.currentThread().getName() +"-||-"+ tmpThead4.getA());

            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("线程3.2->"+ Thread.currentThread().getName() +"-||-"+ tmpThead4.getA());
        }
    }

    /**
     * 银行存取款
     * @param args
     */

    public static void main(String[] args) {
//        AtomicInteger a = new AtomicInteger();
        TmpThead4 tmpThead4 = new TmpThead4();
        for (int i = 0; i < 50; i++) {
            new Thread(tmpThead4, "1111-"+i).start();
//            Thread thread = new Thread(()->{
//                int andIncrement = as[0];//a.getAndIncrement();
//                as[0] = andIncrement+1;
//                System.out.println("线程->"+ Thread.currentThread().getName() +"-||-"+ andIncrement);
////                a.compareAndSet(andIncrement,andIncrement+1);
//                System.out.println("线程->"+ Thread.currentThread().getName() +"-||-"+  as[0]);
//            });
//            thread.start();
        }

        System.out.println("线程3.2->"+ Thread.currentThread().getName() +"-||-"+ tmpThead4.getA());
    }
}
