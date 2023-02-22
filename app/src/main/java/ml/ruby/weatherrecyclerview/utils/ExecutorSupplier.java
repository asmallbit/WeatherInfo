package ml.ruby.weatherrecyclerview.utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
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

    private static final ExecutorService instance = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),
            Runtime.getRuntime().availableProcessors(),
            60L, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(Runtime.getRuntime().availableProcessors()),
            new ThreadPoolExecutor.DiscardOldestPolicy());

    public static ExecutorService getExecutor() {
        return instance;
    }
}
