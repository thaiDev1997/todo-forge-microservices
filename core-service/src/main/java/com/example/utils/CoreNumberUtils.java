package com.example.utils;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Objects;

public class CoreNumberUtils {
    public static boolean greaterThanZero(Long number) {
        return Objects.nonNull(number) && NumberUtils.compare(number, 0) == 1;
    }
}
