package captify.test.java;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

import static captify.test.java.TestAssignment.sampleAfter;
import static captify.test.java.TestAssignment.valueAt;
import static org.testng.Assert.assertEquals;

/**
 * Created by sergii vlasiuk on 28.05.16.
 */
public class TestAssignmentTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(TestAssignmentTest.class);

    private List<BigInteger> bigIntegers = new ArrayList<>();

    @BeforeClass
    public void init() {
        LOGGER.info("init test scope");
        bigIntegers.add(new BigInteger("1"));
        bigIntegers.add(new BigInteger("2"));
        bigIntegers.add(new BigInteger("3"));
        bigIntegers.add(new BigInteger("4"));
        bigIntegers.add(new BigInteger("5"));
        bigIntegers.add(new BigInteger("6"));
        bigIntegers.add(new BigInteger("7"));
        bigIntegers.add(new BigInteger("8"));
        bigIntegers.add(new BigInteger("9"));
    }

    @AfterClass
    public void clean() {
        LOGGER.info("clean test scope after test");
        if (bigIntegers != null) {
            bigIntegers.clear();
        }
    }

    @Test
    public void sampleAfterTest() {
        LOGGER.info("[start] sampleAfter");
        LOGGER.trace("Printing all collection");
        bigIntegers.stream().forEach(e -> LOGGER.trace("{}", e));
        Iterator<BigInteger> iterator = bigIntegers.iterator();

        LOGGER.info("skip first two elements in collection");
        iterator.next();
        iterator.next();

        int afterIndex = 3, count = 3;
        LOGGER.info("get shifted iterator after skipping [2 + {}] first elements with count={}", afterIndex, count);
        iterator = sampleAfter(iterator, afterIndex, count);
        LOGGER.info("Printing results");
        AtomicInteger pos = new AtomicInteger(2 + afterIndex); // add 2 because of shifting iterator in the very beginning.
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                .forEach(bigInteger -> {
                    assertEquals(bigInteger, bigIntegers.get(pos.getAndIncrement()), "value is not same");
                    LOGGER.info("{}", bigInteger);
                });
        LOGGER.info("[end] sampleAfter. [completed successfully]");
    }

    @Test
    public void valueAtTest() {
        LOGGER.info("[start] valueAt");
        LOGGER.trace("Printing all collection");
        bigIntegers.stream().forEach(e -> LOGGER.trace("{}", e));
        Iterator<BigInteger> iterator = bigIntegers.iterator();
        LOGGER.info("skip first element in collection");
        iterator.next();

        int position = 4;
        BigInteger bigIntegerResult = valueAt(iterator, position);

        int pos = 1 + position; // add 1 because of shifting iterator
        assertEquals(bigIntegerResult, bigIntegers.get(pos), "value is not same");
        Map<Integer, Object> collect = IntStream.range(1, 5).collect(HashMap<Integer, Object>::new, (m, r) -> {
            m.put(r, r * r);
        }, (m,u) ->{});
        collect.entrySet().stream().forEach(e -> LOGGER.debug("key [{}], value [{}]", e.getKey(), e.getValue()));
        LOGGER.info("[end] valueAt. [completed successfully]");
    }

}
