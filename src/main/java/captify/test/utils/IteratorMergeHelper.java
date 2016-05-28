package captify.test.utils;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * Created by sergii vlasiuk on 28.05.16.
 *
 */
public class IteratorMergeHelper <T extends Comparable> {
    private T left = null, right = null;
    private Iterator<T> iteratorLeft, iteratorRight;
    private List<T> resultList = new LinkedList<>();

    public IteratorMergeHelper(Iterator<T> iteratorLeft, Iterator<T> iteratorRight) {
        this.iteratorLeft = iteratorLeft;
        this.iteratorRight = iteratorRight;
        left = initNext(iteratorLeft);
        right = initNext(iteratorRight);
    }

    private T initNext(Iterator<T> iteratorLeft) {
        T result = null;
        if (iteratorLeft.hasNext()) {
            result = iteratorLeft.next();
        }
        return result;
    }

    private boolean hasNext() {
        return !isNull(left) || !isNull(right);
    }

    private T next() {
        T result = null;
        if (hasNext()) {
            if (isNull(left)) {
                result = right;
                right = next(iteratorRight);
            } else if (isNull(right)) {
                result = left;
                left = next(iteratorLeft);
            } else if (left.compareTo(right) < 0) {
                result = left;
                left = next(iteratorLeft);
            } else {
                result = right;
                right = next(iteratorRight);
            }
        }
        return result;
    }

    private T next(Iterator<T> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }

    private boolean isNull(T bigInteger) {
        return Objects.isNull(bigInteger);
    }

    public List<T> build() {
        while (hasNext()) {
            resultList.add(next());
        }
        return resultList;
    }
}
