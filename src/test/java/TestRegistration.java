import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import testModels.TestLoginRequest;
import testModels.TestRegisterRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import testModels.TestUser;


public class TestRegistration {
    private static String urlRegistration ="http://127.0.0.1:8080/user/register";
    private String urlLogin="http://127.0.0.1:8080/user/login";
    private static String userAuthToken;
    static TestRegisterRequest testRegisterRequest;
    static TestLoginRequest testLoginRequest;
    private static TestUser testUser;
    static ObjectMapper objectMapper = new ObjectMapper();


    @BeforeAll
    static void setup() throws IOException {
        makeTestData();
        registerTestUser();
    }

    public static void makeTestData() {
        testUser = new TestUser(
                "qwerty1",
                "qwerty1@yandex.ru",
                "simplepassword1"
        );
    }

    public static HttpResponse sendPOSTRequest(String url, String json) throws IOException {
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpPost postRequest= new HttpPost(url);
        StringEntity postBody = new StringEntity(json);
        postRequest.setEntity(postBody);
        postRequest.setHeader("Content-type", "application/json");
        return httpClient.execute(postRequest);
    }

    public HttpResponse sendGETRequest(String url) throws IOException {
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpGet getRequest= new HttpGet(url);
        getRequest.setHeader("Authorization", "application/json");
        return httpClient.execute(getRequest);
    }




    public static void registerTestUser() throws IOException {
        String errorMessage="";
        testRegisterRequest=new TestRegisterRequest(
                testUser.getLogin(),
                testUser.getEmail(),
                testUser.getHashPassword()
        );
        String json = objectMapper.writeValueAsString(testRegisterRequest);
        HttpResponse response = sendPOSTRequest(urlRegistration, json);

        String jsonResponse = IOUtils.toString(response.getEntity().getContent());
        HashMap<String, String> jsonMap = objectMapper.readValue(
                jsonResponse,
                new TypeReference<HashMap<String,String>>() {}
        );
        if (jsonMap.containsKey("auth_token")) {
            userAuthToken =jsonMap.get("auth_token").split(" ")[1];
        }
        if (jsonMap.containsKey("err-message")) {
            errorMessage="\n"+jsonMap.get("err-message");
        }
        Assertions.assertNotNull(userAuthToken, "При регистрации тестового пользователя " +
                "токен авторизации не получен." + errorMessage);
    }


    public String loginTestUser() throws IOException {
        String authToken = null;
        testLoginRequest= new TestLoginRequest(
                testUser.getLogin(),
                testUser.getHashPassword()
        );
        String json = objectMapper.writeValueAsString(testLoginRequest);
        HttpResponse response = sendPOSTRequest(urlLogin, json);

        String jsonResponse = IOUtils.toString(response.getEntity().getContent());
        HashMap<String, String> jsonMap = objectMapper.readValue(
                jsonResponse,
                new TypeReference<HashMap<String,String>>() {}
        );
        if (jsonMap.containsKey("auth_token")) {
            authToken=jsonMap.get("auth_token").split(" ")[1];
        }
        Assertions.assertNotNull(authToken, "После авторизации токен не получен");
        return authToken;
    }
    
    public void deleteTestUser() {}

    @Test
    public void testAuthorizationWithLoginPassword() throws IOException {
        Assertions.assertEquals(userAuthToken, loginTestUser());
    }

    public static void main(String[] args) throws IOException {
    }
}
