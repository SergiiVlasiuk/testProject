package captify.test.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by sergii vlasiuk on 28.05.16.
 */
public class ExecutorServiceWrapper {

    private static ExecutorService executor = null;

    public static ExecutorService getExecutor() {
        if (executor == null || executor.isTerminated() || executor.isShutdown()) {
            synchronized (ExecutorServiceWrapper.class) {
                if (executor == null || executor.isTerminated() || executor.isShutdown()) {
                    executor = Executors.newFixedThreadPool(100);
                }
            }
        }
        return executor;
    }
}
