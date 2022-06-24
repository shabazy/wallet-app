package com.tech.api.integration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tech.api.util.StringUtil;

public class StringTestUtil {

    public static String getJsonString(Object object) throws JsonProcessingException {
        return (new ObjectMapper()).writeValueAsString(object);
    }

    public static String generateRandomEmail() {
        return StringUtil.generateTransactionReference(5) + "@ensep.com";
    }

    public static String generateRandomPassword() {
        return StringUtil.generateTransactionReference(12);
    }

}
