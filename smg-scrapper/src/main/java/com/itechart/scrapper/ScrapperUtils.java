package com.itechart.scrapper;

import java.util.HashSet;
import java.util.Set;

public class ScrapperUtils {
    public static <T> void addElementsToSet(Set<T> firstSet, Set<T> secondSet) {
        if (firstSet == null) {
            firstSet = new HashSet<>();
        }
        if (secondSet != null) {
            firstSet.addAll(secondSet);
        }
    }
}
