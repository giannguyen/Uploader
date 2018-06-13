package com.example.Uploader.model;

import org.apache.commons.lang.RandomStringUtils;
import org.junit.Test;

public class ImageTest {


    @Test
    public void givenUsingApache_whenGeneratingRandomStringBounded_thenCorrect() {

        int length = 15;
        boolean useLetters = true;
        boolean useNumbers = true;
        String generatedString = RandomStringUtils.random(length, useLetters, useNumbers);

        System.out.println(generatedString);

        Byte aByte = new Byte((byte) 123);

        System.out.println(aByte);
    }


}