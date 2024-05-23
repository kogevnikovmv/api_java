import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Disabled;
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
    private String urlRegistration ="http://127.0.0.1:8080/user/register";
    private String urlLogin="http://127.0.0.1:8080/user/login";
    private String authToken;
    static TestRegisterRequest testRegisterRequest;
    static TestLoginRequest testLoginRequest;
    static TestUser testUser;
    static ObjectMapper objectMapper = new ObjectMapper();

    @BeforeAll
    static void makeTestData() {
        testUser = new TestUser(
                "qwerty1",
                "qwerty1@yandex.ru",
                "simplepassword1"
        );
    }

    static HttpResponse sendPOSTRequest(String url, String json) throws IOException {
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpPost postRequest= new HttpPost(url);
        StringEntity postBody = new StringEntity(json);
        postRequest.setEntity(postBody);
        postRequest.setHeader("Content-type", "application/json");
        return httpClient.execute(postRequest);
    }

    static HttpResponse sendGETRequest(String url) throws IOException {
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpGet getRequest= new HttpGet(url);
        getRequest.setHeader("Authorization", "application/json");
        return httpClient.execute(getRequest);
    }

    @Test
    public void registerTestUser() throws IOException {
        testRegisterRequest=new TestRegisterRequest(
                testUser.getLogin(),
                testUser.getEmail(),
                testUser.getHashPassword()
        );
        String json = objectMapper.writeValueAsString(testRegisterRequest);
        System.out.println(json);
        HttpResponse response = sendPOSTRequest(urlRegistration, json);

        String jsonResponse = IOUtils.toString(response.getEntity().getContent());
        HashMap<String, String> jsonMap = objectMapper.readValue(
                jsonResponse,
                new TypeReference<HashMap<String,String>>() {}
        );
        if (jsonMap.containsKey("auth_token")) {
            authToken=jsonMap.get("auth_token").split(" ")[1];
        }
        Assertions.assertNotNull(authToken, "Токен авторизации не получен");
    }

    @Test
    @Disabled
    public void loginTestUser() throws IOException {
        testLoginRequest= new TestLoginRequest(
                testRegisterRequest.getLogin(),
                testRegisterRequest.getPassword()
        );
        String json = objectMapper.writeValueAsString(testLoginRequest);
        System.out.println(json);
        HttpResponse response = sendPOSTRequest(urlLogin, json);

        String jsonResponse = IOUtils.toString(response.getEntity().getContent());
        HashMap<String, String> jsonMap = objectMapper.readValue(
                jsonResponse,
                new TypeReference<HashMap<String,String>>() {}
        );
        if (jsonMap.containsKey("Authorization")) {
            authToken=jsonMap.get("Authorization").split(" ")[1];
        }
        Assertions.assertNotNull(authToken, "Токен авторизации не получен");
    }
    
    public void deleteTestUser() {}

    public static void main(String[] args) throws IOException {
    }
}
