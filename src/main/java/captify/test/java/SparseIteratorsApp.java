package captify.test.java;

import captify.test.utils.ExecutorServiceWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

import static captify.test.java.SparseIterators.*;
import static captify.test.java.TestAssignment.*;

public class SparseIteratorsApp {
  private static final Logger LOGGER = LoggerFactory.getLogger(SparseIteratorsApp.class);

  private static void runTests(int sampleAfterNum, int sparsityMin, int sparsityMax, int approximateExtent) {
    final List<Iterator<BigInteger>> iterators =
      Arrays.asList(
        iteratorSparse(2),
        iteratorSparse(3),
        iteratorSparse(5)
      );

    final long mergeStartedAt = System.currentTimeMillis();

    final Iterator<BigInteger> mergedIterator =
      mergeIterators(iterators);

    final Iterator<BigInteger> numbers =
      sampleAfter(mergedIterator, sampleAfterNum, 10);

    final long mergeFinishedAt = System.currentTimeMillis();
    final long mergeMillis = mergeFinishedAt - mergeStartedAt;

    System.out.println("sampled merged iterator after " + sampleAfterNum + " in " + mergeMillis + " millis:");
    LOGGER.info("sampled merged iterator after {} in {} millis:", sampleAfterNum, mergeMillis);
    numbers.forEachRemaining(System.out::println);

    final long approximatesStartedAt = System.currentTimeMillis();
    final Map<Integer, Future<Double>> approximatesRes =
      approximatesFor(sparsityMin, sparsityMax, approximateExtent);
    final long approximatesFinishedAt = System.currentTimeMillis();
    final long approximatesMillis = approximatesFinishedAt - approximatesStartedAt;
    final int cores = Runtime.getRuntime().availableProcessors();

    System.out.println("approximate sparsities in " + approximatesMillis + " millis by " + approximateExtent + " elems with " + cores + " cores:");
    LOGGER.info("approximate sparsities in {} millis by {} elems with {} cores.", approximatesMillis, approximateExtent, cores);
    approximatesRes.entrySet().forEach(e -> {
      final Future<Double> future = e.getValue();
      String futureAsString;
      if (!future.isDone()) {
        LOGGER.info("getting future result for <{}> with delay, because it was not calculated yet", e.getKey());
        futureAsString = getFutureResultAsString(future, 1, TimeUnit.MINUTES);
      } else {
        futureAsString = getFutureResultAsString(future);
      }
      System.out.println(e.getKey() + " -> " + futureAsString);
      LOGGER.info("calculating {} gives result -> {}", e.getKey(), futureAsString);
    });
  }


  private static String getFutureResultAsString(Future<Double> future, long timeOut, TimeUnit unit) {
    String futureAsString;
    try {
      futureAsString = String.valueOf(future.get(timeOut, unit));
    } catch (InterruptedException e1) {
      futureAsString = "<interrupted on get()>";
    } catch (IllegalArgumentException | ExecutionException e1) {
      futureAsString = e1.getCause().getMessage();
    } catch (TimeoutException e) {
      futureAsString = "<incomplete>";
    }
    return futureAsString;
  }
  private static String getFutureResultAsString(Future<Double> future) {
    return getFutureResultAsString(future, 0L, TimeUnit.SECONDS);
  }

  public static void main(String[] args) {
    LOGGER.info("simple less loaded tests");
    //  simple less loaded tests
    runTests(1000000, 2, 8, 1000000);

    LOGGER.info("more intensive tests with just a bit of exceptions (should run in under 15 minutes)");
    //  more intensive tests with just a bit of exceptions (should run in under 15 minutes)
    runTests(10000000, 0, 24, 10000000);
    LOGGER.info("processing is finished.");
    release();
  }

  private static void release() {
    ExecutorServiceWrapper.release();
  }
}
