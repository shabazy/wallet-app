package com.tech.api.integration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.api.dto.user.request.LoginRequestDTO;
import com.tech.api.dto.user.request.RegisterRequestDTO;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class AuthenticationTestUtil {

    private static final String USERS_REGISTER_ENDPOINT = "/api/users/register";
    private static final String USERS_LOGIN_ENDPOINT = "/api/users/login";

    public static String generateToken(String email, String password) throws JsonProcessingException {
        registerUser(email, password);
        LoginRequestDTO loginRequestDTO = generateLoginRequestDTO(email, password);
        Response loginResponse = given().contentType(ContentType.JSON).body(loginRequestDTO).post(USERS_LOGIN_ENDPOINT);
        HashMap<String, Object> responseDataHashMap = loginResponse.jsonPath().get("data");
        return (String) responseDataHashMap.get("accessToken");
    }

    public static void registerUser(String email, String password) throws JsonProcessingException {
        RegisterRequestDTO registerRequestDTO = generateRegisterRequestDTO(email, password);;
        String json = StringTestUtil.getJsonString(registerRequestDTO);
        RequestSpecification requestSpecification = given().contentType(ContentType.JSON);
        Response response = requestSpecification.body(json).post(USERS_REGISTER_ENDPOINT);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    public static LoginRequestDTO generateLoginRequestDTO(String email, String password) {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setEmail(email);
        loginRequestDTO.setPassword(password);
        return loginRequestDTO;
    }

    public static RegisterRequestDTO generateRegisterRequestDTO(String email, String password) {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO();
        registerRequestDTO.setEmail(email); // that's important because any cannot register with the same email.
        registerRequestDTO.setName("Gokhan Ensep"); // that's not important.
        registerRequestDTO.setPassword(password);
        registerRequestDTO.setPhoneNumber("+90326737232"); // that's not important.
        return registerRequestDTO;
    }

}
