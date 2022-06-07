package ml.ruby.weatherrecyclerview.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author: jwhan
 * @createTime: 2022/05/18 10:09 PM
 * @description:
 */
public class ExecutorSupplier {
    private ExecutorSupplier() {

    }

    private static class ExecutorSupplierContainer {
        private static ExecutorService getInstance() {
            // 核心线程数
            int corePoolSize = Runtime.getRuntime().availableProcessors();
            // 最大线程数
            int maximumPoolSize = corePoolSize * 2;
            // 线程存活时间
            long keepAliveTime = 60L;
            // 线程存活时间单位
            TimeUnit unit = TimeUnit.SECONDS;
            // 有界队列
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(corePoolSize / 2);
            // 拒绝策略
            RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardOldestPolicy();

            return new ThreadPoolExecutor(corePoolSize,
                    maximumPoolSize,
                    keepAliveTime, unit, workQueue,
                    new ThreadPoolExecutor.DiscardOldestPolicy());
        }
    }

    public synchronized static ExecutorService getExecutor() {
        return ExecutorSupplierContainer.getInstance();
    }
}
