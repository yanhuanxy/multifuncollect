package com.yanhuanxy.multifunexport.tools.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yym
 */
@SuppressWarnings("DcDataSourceThread")
public class ThreadPoolUtils {
    private static final int corePoolSize = 5;
    private static final int maxThreadSize = 20;

    private ThreadPoolUtils(){
        super();
    }

    public static ThreadPoolExecutor getThreadPool() {
        return ThreadPoolHolder.pool;
    }

    /**
     * 静态内部类
     */
    private static class ThreadPoolHolder {
        private static final ThreadPoolExecutor pool = createPool();

        /**
         * 创建线程池
         *
         * @return ThreadPoolExecutor
         */
        @SuppressWarnings("DcDataSourceThread")
        private static ThreadPoolExecutor createPool() {
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("DcDataSource-pool-%d").build();
            int keepAliveTime = 10;
            return new ThreadPoolExecutor(ThreadPoolUtils.corePoolSize, ThreadPoolUtils.maxThreadSize,
                    keepAliveTime, TimeUnit.SECONDS,
                    new LinkedBlockingQueue<Runnable>(200),namedThreadFactory,
                    new ThreadPoolExecutor.AbortPolicy());
        }
    }


}
