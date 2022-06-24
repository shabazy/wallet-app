package com.tech.api.integration.util;

import io.restassured.response.Response;

import java.util.HashMap;

public class HashMapTestUtil {

    public static HashMap<String, Object> convertResponseToHashMap(Response response) {
        return response.jsonPath().get("data");
    }

}
