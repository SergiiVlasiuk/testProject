import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.math.BigInteger;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.StreamSupport;

import static captify.test.java.TestAssignment.sampleAfter;
import static org.testng.Assert.assertEquals;

/**
 * Created by sergii vlasiuk on 28.05.16.
 */
public class TestAssignmentTest {
    List<BigInteger> bigIntegers = new ArrayList<>();

    @BeforeClass
    public void init() {
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
        if (bigIntegers != null) {
            bigIntegers.clear();
        }
    }

    @Test
    public void sampleAfterTest() {
        bigIntegers.stream().forEach(System.out::println);
        Iterator<BigInteger> iterator = bigIntegers.iterator();

        // skip first two elements in collection
        iterator.next();
        iterator.next();

        int afterIndex = 3, count = 3;
        // needs iterator after skipping 2 first elements limiting 3 in shifted iterator
        iterator = sampleAfter(iterator, afterIndex, count);
        AtomicInteger pos = new AtomicInteger(2 + afterIndex); // add 2 because of shifting iterator
        StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, Spliterator.ORDERED), false)
                .forEach(bigInteger -> {
                    assertEquals(bigInteger, bigIntegers.get(pos.getAndIncrement()), "value is not same");
                });
    }

}
