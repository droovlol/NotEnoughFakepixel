package org.ginafro.notenoughfakepixel.utils;

import java.util.Arrays;
import java.util.List;

public class ListUtils {

    @SafeVarargs
    public static <T> List<T> of(T... items) {
        return Arrays.asList(items);
    }

}
