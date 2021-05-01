package com.example.demo;

import com.example.demo.accounts.Account;
import com.example.demo.accounts.AccountsRepository;
import com.example.demo.accounts.AccountsController;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import io.restassured.RestAssured;
import io.restassured.config.JsonConfig;
import io.restassured.path.json.config.JsonPathConfig;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;

import static org.hamcrest.Matchers.*;
import static io.restassured.config.RedirectConfig.redirectConfig;

import static io.restassured.RestAssured.*;

@TestMethodOrder(OrderAnnotation.class)
public class AccountsTest {
    @BeforeAll
    public static void initClass() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        RestAssured.useRelaxedHTTPSValidation();
        RestAssured.urlEncodingEnabled = false;
        RestAssured.config = RestAssured.config()
            .jsonConfig(JsonConfig.jsonConfig().numberReturnType(JsonPathConfig.NumberReturnType.DOUBLE))
            .redirect(redirectConfig().followRedirects(false));
    }

    /**
     * Test account creation
     * @throws Exception
     */
    @Test
    @Order(1)
    public void testAddAccount_Valid_ReturnJson() throws Exception {
        // add new account and get info
        JSONObject requestParams = new JSONObject();
        // each user can have different accounts
        requestParams.put("customer_id", TestConstants.user_id_1);
        requestParams.put("balance", TestConstants.account_balance_1);
        
        // the available-balance will be computed based on any open buy trades
        // so we do not specify it here
        
        // Get newly created account id
        TestConstants.account_id_1 = 
        given().auth().basic(TestConstants.m_USERNAME, TestConstants.m_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .post(TestConstants.accountURL)
                .then()
                .statusCode(201)
                .body("balance", equalTo(TestConstants.account_balance_1))
                .extract().path("id");

        // view by user - unsuccessful due to no authentication
         given().accept("*/*")
                .contentType("application/json")
                .get(TestConstants.accountURL + "/" + TestConstants.account_id_1)
                .then()
                .statusCode(401);

        // view by analyst - forbidden
        given().auth().basic(TestConstants.a1_USERNAME, TestConstants.a1_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .get(TestConstants.accountURL + "/" + TestConstants.account_id_1)
                .then()
                .statusCode(403);

        // view by this user - successful
        given().auth().basic(TestConstants.u1_USERNAME, TestConstants.u1_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .get(TestConstants.accountURL + "/" + TestConstants.account_id_1)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("balance", equalTo(TestConstants.account_balance_1));
    }

//     /**
//      * Test account creation: invalid data, return 400 - Bad Request
//      * @throws Exception
//      */
    @Test
    @Order(2)
    public void testAddAccount_Invalid_Return400() throws Exception{
        JSONObject requestParams = new JSONObject();
        // invalid customer id (does not exist) - return 400 Bad Request
        requestParams.put("customer_id", 987650);
        requestParams.put("balance", 50000);

        // available balance specified here will be igored by the API
        requestParams.put("avail_balance", 100000);
            
        given().auth().basic(TestConstants.m_USERNAME, TestConstants.m_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .post(TestConstants.accountURL)
                .then()
                .statusCode(400);
    }
    
//     /**
//      * Test transfer funds - success
//      * If invalid parameters specified for the transfer - return 400 Bad Request
//      * Transfer by posting to URL: accountURL + "/{accountId}/transactions"
//      */
    @Test
    @Order(3)
    public void testTransfer_Valid_ReturnJson() throws Exception{
        // First, create a new acccount for user2
        JSONObject requestParams = new JSONObject();
        requestParams.put("customer_id", TestConstants.user_id_2);
        requestParams.put("balance", TestConstants.account_balance_2);
            
        TestConstants.account_id_2 = 
        given().auth().basic(TestConstants.m_USERNAME, TestConstants.m_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .post(TestConstants.accountURL)
                .then()
                .statusCode(201)
                .body("balance", equalTo(TestConstants.account_balance_2))
                .extract().path("id");
        
        // transfer from account of user1 to account of user2
        double amount = 10000.0;
        requestParams = new JSONObject();
        requestParams.put("from", TestConstants.account_id_1);
        requestParams.put("to", TestConstants.account_id_2);
        // amount has to be > 0, otherwise it is a bad request (status 400)
        requestParams.put("amount", amount);
        given().auth().basic(TestConstants.u1_USERNAME, TestConstants.u1_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .post(TestConstants.accountURL + "/" + TestConstants.account_id_1 + "/transactions")
                .then()
                .statusCode(201);
        
        // check balance of user1's account
        given().auth().basic(TestConstants.u1_USERNAME, TestConstants.u1_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .get(TestConstants.accountURL + "/" + TestConstants.account_id_1)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("balance", equalTo(TestConstants.account_balance_1 - amount));

        // check balance of user2's acount
        given().auth().basic(TestConstants.u2_USERNAME, TestConstants.u2_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .get(TestConstants.accountURL + "/" + TestConstants.account_id_2)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("balance", equalTo(TestConstants.account_balance_2 + amount));
        
        
        // transfer back the same amount
        // transfer from account 2 to account 1
        requestParams = new JSONObject();
        requestParams.put("from", TestConstants.account_id_2);
        requestParams.put("to", TestConstants.account_id_1);
        // amount has to be > 0
        requestParams.put("amount", amount);
        given().auth().basic(TestConstants.u2_USERNAME, TestConstants.u2_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .body(requestParams.toJSONString())
                .post(TestConstants.accountURL + "/" + TestConstants.account_id_2 + "/transactions")
                .then()
                .statusCode(201);
        
        // check balance of account 1
        given().auth().basic(TestConstants.u1_USERNAME, TestConstants.u1_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .get(TestConstants.accountURL + "/" + TestConstants.account_id_1)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("balance", equalTo(TestConstants.account_balance_1));

        // check balance of account 2
        given().auth().basic(TestConstants.u2_USERNAME, TestConstants.u2_PASSWORD)
                .accept("*/*")
                .contentType("application/json")
                .get(TestConstants.accountURL + "/" + TestConstants.account_id_2)
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body("balance", equalTo(TestConstants.account_balance_2));
    }

    // There can be more tests for valid/invalid transfers, view past transactions, etc.
}