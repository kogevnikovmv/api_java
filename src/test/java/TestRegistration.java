import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Disabled;
import testModels.*;
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


public class TestRegistration {
    private static String urlRegistration ="http://127.0.0.1:8080/user/register";
    private static String urlLogin="http://127.0.0.1:8080/user/login";
    private static String urlHomePage="http://127.0.0.1:8080";
    private static String urlChangePassword="http://127.0.0.1:8080/user/chng-psswrd";
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

    public static HashMap<String, String> sendPOSTRequest(String url, Request userRequest, String token) throws IOException {

        String json = objectMapper.writeValueAsString(userRequest);

        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpPost postRequest= new HttpPost(url);
        if (token!=null) {
            postRequest.setHeader("Authorization", "Bearer "+token);
        }
        StringEntity postBody = new StringEntity(json);
        postRequest.setEntity(postBody);
        postRequest.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(postRequest);

        String jsonResponse = IOUtils.toString(response.getEntity().getContent());
        HashMap<String, String> jsonMap = objectMapper.readValue(
                jsonResponse,
                new TypeReference<HashMap<String,String>>() {}
        );
        return jsonMap;
    }

    public HashMap<String, String> sendGETRequest(String url, String token) throws IOException {
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpGet getRequest= new HttpGet(url);
        if (token!=null) {
            getRequest.setHeader("Authorization", "Bearer "+token);
        }
        HttpResponse response = httpClient.execute(getRequest);

        String jsonResponse = IOUtils.toString(response.getEntity().getContent());
        HashMap<String, String> jsonMap = objectMapper.readValue(
                jsonResponse,
                new TypeReference<HashMap<String,String>>() {}
        );
        return jsonMap;
    }




    public static void registerTestUser() throws IOException {
        String errorMessage="";
        testRegisterRequest=new TestRegisterRequest(
                testUser.getLogin(),
                testUser.getEmail(),
                testUser.getHashPassword()
        );

        HashMap<String, String> jsonMap = sendPOSTRequest(urlRegistration, testRegisterRequest, null);

        if (jsonMap.containsKey("auth_token")) {
            userAuthToken =jsonMap.get("auth_token").split(" ")[1];
        }

        if (jsonMap.containsKey("message")) {
            errorMessage="\n"+jsonMap.get("message");
        }

        Assertions.assertNotNull(userAuthToken, "При регистрации тестового пользователя " +
                "токен авторизации не получен." + errorMessage);
    }

    @Test
    public void testAuthorizationWithLoginPassword() throws IOException {
        String errorMessage="";
        String authToken = null;
        testLoginRequest= new TestLoginRequest(
                testUser.getLogin(),
                testUser.getHashPassword()
        );

        HashMap<String, String> jsonMap = sendPOSTRequest(urlLogin, testLoginRequest, null);

        if (jsonMap.containsKey("auth_token")) {
            authToken=jsonMap.get("auth_token").split(" ")[1];
        }

        if (jsonMap.containsKey("message")) {
            errorMessage="\n"+jsonMap.get("message");
        }

        Assertions.assertNotNull(authToken, "После авторизации токен не получен."+ errorMessage);
        Assertions.assertEquals(userAuthToken, authToken);
    }

    @Test
    public void testAuthorizationWithToken() throws IOException {
        String errorMessage="";
        String message = null;

        HashMap <String, String> jsonMap=sendGETRequest(urlHomePage, userAuthToken);

        if (jsonMap.containsKey("msg")) {
            message=jsonMap.get("msg");
        }

        if (jsonMap.containsKey("message")) {
            errorMessage="\n"+jsonMap.get("message");
        }


        Assertions.assertEquals("Hello, "+testUser.getLogin()+"!", message, errorMessage);
    }

    @Test
    public void testUserChangePassword() throws IOException {
        String errorMessage="";
        String authToken=null;

        var testChangePasswordRequest = new TestChangePasswordRequest(
                "MyNewPerfectPassword"
        );

        HashMap<String, String> jsonMap = sendPOSTRequest(urlChangePassword, testChangePasswordRequest, userAuthToken);

        if (jsonMap.containsKey("auth_token")) {
            authToken=jsonMap.get("auth_token").split(" ")[1];
        }

        if (jsonMap.containsKey("message")) {
            errorMessage="\n"+jsonMap.get("message");
        }


        Assertions.assertNotNull(authToken, "При смене пароля новый " +
                "токен авторизации не получен." + errorMessage);
        Assertions.assertNotEquals(userAuthToken, authToken,
                "Старый токен совпадает с новым." + errorMessage);

        testUser.setHashPassword(testChangePasswordRequest.getPassword());
        userAuthToken=authToken;

    }
    @BeforeAll
    public static void deleteTestUser() {

    }

    public static void main(String[] args) throws IOException {
    }
}
