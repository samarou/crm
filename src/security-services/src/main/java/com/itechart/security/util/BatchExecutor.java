package com.itechart.security.util;

import java.util.Collections;
import java.util.List;

/**
 * Utility for batch processing a collection of objects
 *
 * @author andrei.samarou
 */
public class BatchExecutor {

    public static <T> void execute(Executable<T> executable, List<T> objects, int batchSize) {
        int objectsCount = objects.size();
        if (objectsCount <= batchSize || batchSize <= 0) {
            executable.execute(Collections.unmodifiableList(objects));
        } else {
            for (int i = 0, count = objectsCount / batchSize; i < count; i++) {
                int from = i * batchSize;
                List<T> batch = objects.subList(from, from + batchSize);
                executable.execute(Collections.unmodifiableList(batch));
            }
            int count = objectsCount % batchSize;
            if (count > 0) {
                List<T> batch = objects.subList(objectsCount - count, objectsCount);
                executable.execute(Collections.unmodifiableList(batch));
            }
        }
    }

    public interface Executable<T> {
        void execute(List<T> batch);
    }
}