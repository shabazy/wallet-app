package com.tech.api.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.api.dto.user.request.LoginRequestDTO;
import com.tech.api.dto.user.request.RegisterRequestDTO;
import com.tech.api.integration.util.AuthenticationTestUtil;
import com.tech.api.integration.util.HashMapTestUtil;
import com.tech.api.integration.util.StringTestUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;


import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
class UserControllerTest {

    private static final String USERS_REGISTER_ENDPOINT = "/api/users/register";
    private static final String USERS_LOGIN_ENDPOINT = "/api/users/login";

    @LocalServerPort
    private int port;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
    }

    @Test
    public void testRegisterSuccess() throws JsonProcessingException {
        String email = StringTestUtil.generateRandomEmail();
        String password = StringTestUtil.generateRandomPassword();
        RegisterRequestDTO registerRequestDTO = AuthenticationTestUtil.generateRegisterRequestDTO(email, password);;
        String json = StringTestUtil.getJsonString(registerRequestDTO);
        Response response = given().contentType(ContentType.JSON).body(json).post(USERS_REGISTER_ENDPOINT);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
    }

    @Test
    public void testDuplicatedRegisterError() throws JsonProcessingException {
        String email = StringTestUtil.generateRandomEmail();
        String password = StringTestUtil.generateRandomPassword();
        RegisterRequestDTO registerRequestDTO = AuthenticationTestUtil.generateRegisterRequestDTO(email, password);;
        String json = StringTestUtil.getJsonString(registerRequestDTO);
        RequestSpecification requestSpecification = given().contentType(ContentType.JSON).body(json);
        Response response = requestSpecification.post(USERS_REGISTER_ENDPOINT);
        assertEquals(HttpStatus.CREATED.value(), response.getStatusCode());
        Response response2 = requestSpecification.post(USERS_REGISTER_ENDPOINT);
        assertEquals(HttpStatus.BAD_REQUEST.value(), response2.getStatusCode());
    }

    @Test
    public void testRegisterAndLoginSuccess() throws JsonProcessingException {
        String email = StringTestUtil.generateRandomEmail();
        String password = StringTestUtil.generateRandomPassword();
        AuthenticationTestUtil.registerUser(email, password);
        LoginRequestDTO loginRequestDTO = AuthenticationTestUtil.generateLoginRequestDTO(email, password);
        String loginJson = StringTestUtil.getJsonString(loginRequestDTO);
        Response loginResponse = given().contentType(ContentType.JSON).body(loginJson).post(USERS_LOGIN_ENDPOINT);
        assertEquals(HttpStatus.OK.value(), loginResponse.getStatusCode());
        HashMap<String, Object> loginHashMap = HashMapTestUtil.convertResponseToHashMap(loginResponse);
        assertTrue(loginHashMap.containsKey("accessToken"));
    }

    @Test
    public void testRegisterAndLoginError() throws JsonProcessingException {
        String email = StringTestUtil.generateRandomEmail();
        String password = StringTestUtil.generateRandomPassword();
        AuthenticationTestUtil.registerUser(email, password);
        LoginRequestDTO loginRequestDTO = AuthenticationTestUtil.generateLoginRequestDTO(email, "wrongpassword!");
        String loginJson = StringTestUtil.getJsonString(loginRequestDTO);
        Response loginResponse = given().contentType(ContentType.JSON).body(loginJson).post(USERS_LOGIN_ENDPOINT);
        assertEquals(HttpStatus.BAD_REQUEST.value(), loginResponse.getStatusCode());
    }

}
