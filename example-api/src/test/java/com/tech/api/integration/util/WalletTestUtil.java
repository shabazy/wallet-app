package com.tech.api.integration.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.api.dto.wallet.request.CreateWalletRequestDTO;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.springframework.http.HttpStatus;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class WalletTestUtil {

    private static final String WALLET_CREATE_ENDPOINT = "/api/wallets";

    public static HashMap<String, Object> createWallet(String walletName, String accessToken) throws JsonProcessingException {
        CreateWalletRequestDTO createWalletRequestDTO = new CreateWalletRequestDTO();
        createWalletRequestDTO.setName(walletName);
        String walletJson = StringTestUtil.getJsonString(createWalletRequestDTO);
        Response walletResponse = given()
                .header(new Header("Authorization", "Bearer " + accessToken))
                .contentType(ContentType.JSON)
                .body(walletJson)
                .post(WALLET_CREATE_ENDPOINT);
        assertEquals(HttpStatus.CREATED.value(), walletResponse.getStatusCode());
        return HashMapTestUtil.convertResponseToHashMap(walletResponse);
    }

}
