package captify.test.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by sergii vlasiuk on 28.05.16.
 */
public class ExecutorServiceWrapper {
    private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorServiceWrapper.class);

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

    public static void release() {
        synchronized (ExecutorServiceWrapper.class) {
            if (executor != null && !(executor.isTerminated() || executor.isShutdown())) {
                List<Runnable> runnables = executor.shutdownNow();
//                executor.awaitTermination(15, TimeUnit.SECONDS);
                runnables.stream().forEach(runnable -> LOGGER.debug("in progress yet: {}", runnable));
            }
        }
    }
}
