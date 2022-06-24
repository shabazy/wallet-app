package com.tech.api.integration;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.tech.api.dto.transaction.request.CreateWalletTransactionRequestDTO;
import com.tech.api.dto.transaction.request.SendMoneyRequestDTO;
import com.tech.api.entity.WalletTransactionType;
import com.tech.api.integration.util.AuthenticationTestUtil;
import com.tech.api.integration.util.HashMapTestUtil;
import com.tech.api.integration.util.StringTestUtil;
import com.tech.api.integration.util.WalletTestUtil;
import com.tech.api.util.StringUtil;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("dev")
public class WalletTransactionControllerTest {

    @LocalServerPort
    private int port;

    private String accessToken;

    @BeforeEach
    public void setUp() throws JsonProcessingException {
        RestAssured.port = port;
        String email = StringUtil.generateTransactionReference(5) + "@ensep.com";
        String password = StringUtil.generateTransactionReference(12);
        this.accessToken = AuthenticationTestUtil.generateToken(email, password);
    }

    @Test
    public void testCreateTopUpTransaction() throws JsonProcessingException {

        HashMap<String, Object> walletResponseDataHashMap = WalletTestUtil.createWallet("my wallet", this.accessToken);
        int walletId = (int) walletResponseDataHashMap.get("id");
        BigDecimal amount = BigDecimal.valueOf(10.22);
        createTransaction(WalletTransactionType.TOP_UP, walletId, amount);

        Response walletResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .get( "/api/wallets/" + walletId);
        assertEquals(HttpStatus.OK.value(), walletResponse.getStatusCode());
        assertEquals(amount.floatValue(), HashMapTestUtil.convertResponseToHashMap(walletResponse).get("balance"));

    }

    @Test
    public void testCreatePaymentTransaction() throws JsonProcessingException {

        HashMap<String, Object> walletResponseDataHashMap = WalletTestUtil.createWallet("my wallet", this.accessToken);
        int walletId = (int) walletResponseDataHashMap.get("id");

        BigDecimal topUpAmount = BigDecimal.valueOf(10.22);
        createTransaction(WalletTransactionType.TOP_UP, walletId, topUpAmount);

        BigDecimal paymentAmount = BigDecimal.valueOf(5.22);
        // topping the payment up
        createTransaction(WalletTransactionType.PAYMENT,walletId, paymentAmount);

        // checking the top-up process
        Response walletResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .get( "/api/wallets/" +walletId);
        assertEquals(HttpStatus.OK.value(), walletResponse.getStatusCode());
        assertEquals(topUpAmount.subtract(paymentAmount).floatValue(), HashMapTestUtil.convertResponseToHashMap(walletResponse).get("balance"));
    }

    @Test
    public void testSendMoney() throws JsonProcessingException {
        HashMap<String, Object> sourceWalletResponseDataHashMap = WalletTestUtil.createWallet("source wallet", this.accessToken);
        HashMap<String, Object> targetWalletResponseDataHashMap = WalletTestUtil.createWallet("target wallet", this.accessToken);

        // creating the wallet
        int sourceWalletId = (int) sourceWalletResponseDataHashMap.get("id");
        int targetWalletId = (int) targetWalletResponseDataHashMap.get("id");

        BigDecimal topUpAmount = BigDecimal.valueOf(10.22);
        createTransaction(WalletTransactionType.TOP_UP, sourceWalletId, topUpAmount);

        BigDecimal transferAmount = BigDecimal.valueOf(5.22);
        SendMoneyRequestDTO sendMoneyRequestDTO = new SendMoneyRequestDTO();
        sendMoneyRequestDTO.setAmount(transferAmount);
        sendMoneyRequestDTO.setTargetWalletSerialNumber(targetWalletResponseDataHashMap.get("serial_number").toString());

        String sendMoneyTransactionJson = StringTestUtil.getJsonString(sendMoneyRequestDTO);
        Response moneyTransferResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .contentType(ContentType.JSON)
                .body(sendMoneyTransactionJson)
                .post("/api/wallets/" + sourceWalletId + "/transactions/send-money");

        HashMap<String, Object> moneyTransferHashMap = HashMapTestUtil.convertResponseToHashMap(moneyTransferResponse);
        assertTrue(moneyTransferHashMap.containsKey("source"));
        assertTrue(moneyTransferHashMap.containsKey("target"));

        Response sourceWalletResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .get( "/api/wallets/" + sourceWalletId);

        HashMap<String, Object> sourceWalletHashMap = HashMapTestUtil.convertResponseToHashMap(sourceWalletResponse);
        assertEquals(topUpAmount.subtract(transferAmount).floatValue(), sourceWalletHashMap.get("balance"));

        Response targetWalletResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .get( "/api/wallets/" + targetWalletId);
        HashMap<String, Object> targetWalletHashMap = HashMapTestUtil.convertResponseToHashMap(targetWalletResponse);
        assertEquals(transferAmount.floatValue(), targetWalletHashMap.get("balance"));
    }


    private void createTransaction(WalletTransactionType walletTransactionType, int walletId, BigDecimal amount) throws JsonProcessingException {
        CreateWalletTransactionRequestDTO createWalletTransactionRequestDTO = new CreateWalletTransactionRequestDTO(
                walletTransactionType,
                amount
        );
        String walletTransactionJson = StringTestUtil.getJsonString(createWalletTransactionRequestDTO);
        Response walletTransactionResponse = given()
                .header(new Header("Authorization", "Bearer " + this.accessToken))
                .contentType(ContentType.JSON)
                .body(walletTransactionJson)
                .post("/api/wallets/" + walletId + "/transactions");
        HashMap<String, Object> walletTransactionResponseHashMap = HashMapTestUtil.convertResponseToHashMap(walletTransactionResponse);
        assertEquals(HttpStatus.OK.value(), walletTransactionResponse.getStatusCode());
        assertEquals(amount.floatValue(), walletTransactionResponseHashMap.get("amount"));
        assertEquals(walletTransactionType.getName(), walletTransactionResponseHashMap.get("transaction_type"));
    }

}
