package com.example.utils;

import org.apache.commons.lang3.StringUtils;

public class CoreString {
    public static boolean test(String str) {
        return StringUtils.isNotBlank(str);
    }
}
