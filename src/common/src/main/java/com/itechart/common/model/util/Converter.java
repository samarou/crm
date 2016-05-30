package com.itechart.common.model.util;

import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class Converter {

    public static <A, B> List<B> convertCollection(Collection<A> list, Function<A, B> conversion) {
        if (CollectionUtils.isEmpty(list)) {
            return Collections.emptyList();
        }
        return list.stream().map(conversion).collect(Collectors.toList());
    }
}
