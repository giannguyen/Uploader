package com.example.Uploader.utils;

import org.apache.commons.lang.RandomStringUtils;

public class StringUtils {

    public static final int RAN_LENGTH = 15;

    public static String randomImageName(){
        boolean useLetters = true;
        boolean useNumbers = true;
        return RandomStringUtils.random(RAN_LENGTH, useLetters, useNumbers);
    }
}
