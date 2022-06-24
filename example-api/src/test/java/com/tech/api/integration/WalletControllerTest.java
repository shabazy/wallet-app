package com.tech.api.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.api.dto.wallet.request.CreateWalletRequestDTO;
import com.tech.api.dto.wallet.request.UpdateWalletRequestDTO;
import com.tech.api.integration.util.AuthenticationTestUtil;
import com.tech.api.integration.util.HashMapTestUtil;
import com.tech.api.integration.util.StringTestUtil;
import com.tech.api.integration.util.WalletTestUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class WalletControllerTest {

    @LocalServerPort
    private int port;

    private String accessToken;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        RestAssured.port = port;
        String email = StringTestUtil.generateRandomEmail();
        String password = StringTestUtil.generateRandomPassword();
        this.accessToken = AuthenticationTestUtil.generateToken(email, password);
    }

    @Test
    public void testCreateWallet() throws JsonProcessingException {
        String walletName = "my wallet";
        HashMap<String, Object> walletResponseDataHashMap = WalletTestUtil.createWallet(walletName, this.accessToken);
        assertEquals(walletName, walletResponseDataHashMap.get("name"));
    }

    @Test
    public void testGetWallet() throws JsonProcessingException {
        String walletName = "my wallet";
        HashMap<String, Object> walletResponseDataHashMap = WalletTestUtil.createWallet(walletName, this.accessToken);
        Response walletResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .get( "/api/wallets/" + walletResponseDataHashMap.get("id"));
        assertEquals(HttpStatus.OK.value(), walletResponse.getStatusCode());

    }

    @Test
    public void testGetWallets() throws JsonProcessingException {
        String walletName = "my wallet";
        WalletTestUtil.createWallet(walletName, this.accessToken);
        Response walletResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .get( "/api/wallets");
        assertEquals(HttpStatus.OK.value(), walletResponse.getStatusCode());
        HashMap<String, Object> walletData = walletResponse.jsonPath().get("data");
        assertEquals(1, walletData.get("total_element_size"));
    }

    @Test
    public void testUpdateWallet() throws JsonProcessingException {
        String walletName = "my wallet";
        HashMap<String, Object> walletResponseDataHashMap = WalletTestUtil.createWallet(walletName, this.accessToken);

        UpdateWalletRequestDTO updateWalletRequestDTO = new UpdateWalletRequestDTO();
        String updatedWalletName = "updated wallet name";
        updateWalletRequestDTO.setName(updatedWalletName);
        String walletJson = StringTestUtil.getJsonString(updateWalletRequestDTO);
        Response walletResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .contentType(ContentType.JSON)
                .body(walletJson)
                .put( "/api/wallets/" + walletResponseDataHashMap.get("id"));
        HashMap<String, Object> walletHashMap = HashMapTestUtil.convertResponseToHashMap(walletResponse);
        assertEquals(HttpStatus.OK.value(), walletResponse.getStatusCode());
        assertEquals(updatedWalletName, walletHashMap.get("name"));
    }

}
