package com.tech.api.util;

import org.apache.commons.lang3.RandomStringUtils;

public class StringUtil {

    public static String generateTransactionReference(int length) {
        return RandomStringUtils.randomAlphanumeric(length).toUpperCase();
    }

}
