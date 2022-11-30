package com.example.demo.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NumberUtils {

    public static boolean isNegativeOrNull(Integer num){
        return num == null || num < 0;
    }

    public static boolean isNumNegativeOrNull(Number num) {
        return num == null || num.intValue() < 0;
    }

    public static boolean isPositive(Integer num){
        return num != null && num > 0;
    }

    public static boolean isNotPositive(Integer num){
        if (num == null || num <= 0) {
            return true;
        }
        return false;
    }

    public static boolean isNotPositive(Long num){
        if (num == null || num <= 0) {
            return true;
        }
        return false;
    }
}
