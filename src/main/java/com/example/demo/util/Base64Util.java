package com.example.demo.util;

/**
 * @author wuziwei
 * @description
 * @date 2022/12/6
 */

import java.io.UnsupportedEncodingException;
import java.util.Base64;

public class Base64Util {

    public static String encode(String text) {
        try {
            return Base64.getEncoder().encodeToString(text.getBytes("utf-8"));
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error :" + e.getMessage());
            return "";
        }
    }

    public static String decode(String text) {
        try {
            byte[] base64decodedBytes = Base64.getDecoder().decode(text);
            return new String(base64decodedBytes, "utf-8");
        } catch (UnsupportedEncodingException e) {
            System.out.println("Error :" + e.getMessage());
            return "";
        }
    }

    public static void main(String[] args) throws Exception {
        String text = "teacher";
        String encodeText = encode(text);
        String decodeText = decode(encodeText);
        System.out.println("encodeText : " + encodeText);
        System.out.println("decodeText : " + decodeText);
    }
}

