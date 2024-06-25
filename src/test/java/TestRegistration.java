import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.io.IOUtils;
import org.apache.http.client.methods.HttpGet;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import testModels.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
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
    private static TestUser testUser;
    private static final ObjectMapper objectMapper = new ObjectMapper();


    @BeforeAll
    static void setup() throws IOException {
        makeTestData();
        registerTestUser();
    }

    public static void makeTestData() {
        testUser = new TestUser(
                "qwerty2",
                "qwerty2@yandex.ru",
                "S!mplepassword1"
        );
    }

    public static JsonNode sendPOSTRequest(String url, Request userRequest, String token) throws IOException {

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


        JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());

        return jsonResponse;
    }

    public JsonNode sendPOSTRequestTree(String url, Request userRequest, String token) throws IOException {

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

        JsonNode jsonMap = objectMapper.readTree(response.getEntity().getContent());

        return jsonMap;
    }

    public JsonNode sendGETRequest(String url, String token) throws IOException {
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpGet getRequest= new HttpGet(url);
        if (token!=null) {
            getRequest.setHeader("Authorization", "Bearer "+token);
        }
        HttpResponse response = httpClient.execute(getRequest);

        JsonNode jsonResponse = objectMapper.readTree(response.getEntity().getContent());

        return jsonResponse;
    }




    public static void registerTestUser() throws IOException {
        String errorMessage="";
        var testRegisterRequest=new TestRegisterRequest(
                testUser.getLogin(),
                testUser.getEmail(),
                testUser.getHashPassword()
        );

        JsonNode jsonResponse = sendPOSTRequest(urlRegistration, testRegisterRequest, null);
        System.out.println(jsonResponse.toString());
        if (jsonResponse.has("auth_token")) {
            userAuthToken =jsonResponse.get("auth_token").toString().replace("\"", "").split(" ")[1];
            System.out.println(userAuthToken);
        }

        if (jsonResponse.has("message")) {
            errorMessage="\n"+jsonResponse.get("message").toString();
        }

        Assertions.assertNotNull(userAuthToken, "При регистрации тестового пользователя " +
                "токен авторизации не получен." + errorMessage);
    }

    @Test
    public void testAuthorizationWithLoginPassword() throws IOException {
        String errorMessage="";
        String authToken = null;
        var testLoginRequest= new TestLoginRequest(
                testUser.getLogin(),
                testUser.getHashPassword()
        );

        JsonNode jsonResponse = sendPOSTRequest(urlLogin, testLoginRequest, null);

        if (jsonResponse.has("auth_token")) {
            authToken=jsonResponse.get("auth_token").toString().replace("\"", "").split(" ")[1];
        }

        if (jsonResponse.has("errors")) {
            errorMessage="\n"+jsonResponse.get("errors").toString();
        }

        Assertions.assertNotNull(authToken, "После авторизации токен не получен."+ errorMessage);
        Assertions.assertEquals(userAuthToken, authToken);
    }

    @Test
    public void testAuthorizationWithToken() throws IOException {
        String errorMessage="";
        String message = null;

        JsonNode jsonResponse = sendGETRequest(urlHomePage, userAuthToken);
        System.out.println(jsonResponse.toString());
        if (jsonResponse.has("msg")) {
            message=jsonResponse.get("msg").toString().replace("\"", "");
            System.out.println(message);
        }

        if (jsonResponse.has("message")) {
            errorMessage="\n"+jsonResponse.get("message").toString().replace("\"", "");
        }


        Assertions.assertEquals("Hello, "+testUser.getLogin()+"!",
                message,
                errorMessage);
    }

    @Test
    public void testUserChangePassword() throws IOException {
        String errorMessage="";
        String authToken=null;

        var testChangePasswordRequest = new TestChangePasswordRequest(
                "MyNewPerfectPassword"
        );

        JsonNode jsonResponse = sendPOSTRequest(urlChangePassword, testChangePasswordRequest, userAuthToken);

        if (jsonResponse.has("auth_token")) {
            authToken=jsonResponse.get("auth_token").toString().replace("\"", "").split(" ")[1];
        }

        if (jsonResponse.has("errors")) {
            errorMessage="\n"+jsonResponse.get("errors").toString();
        }


        Assertions.assertNotNull(authToken, "При смене пароля новый " +
                "токен авторизации не получен." + errorMessage);
        Assertions.assertNotEquals(userAuthToken, authToken,
                "Старый токен совпадает с новым." + errorMessage);

        testUser.setHashPassword(testChangePasswordRequest.getPassword());
        userAuthToken=authToken;

    }

    @Disabled //ошибка
    @ParameterizedTest(name="[{index}] {arguments}")
    @CsvSource(value={"t, @yandex.ru, testpassword1!",
                "te, testUser2@, T!stpa7",
                "testUser4qwert16, testUser4yandex.ru, TESTPASSWORD1!",
                "testUser5qwerty17, testUser5@yandex, T!stpassw0rdqwertyuiopasdfghj31"
    }, ignoreLeadingAndTrailingWhitespace=false)
    //не все варианты добавил
    public void registerNoValidUser(String testLogin, String testEmail, String testPassword) throws IOException {
        String errorMessage="";
        String authToken=null;
        var testRegisterRequest=new TestRegisterRequest(
                testLogin,
                testEmail,
                testPassword
        );

        JsonNode jsonMap = sendPOSTRequest(urlRegistration, testRegisterRequest, null);

        if (jsonMap.has("auth_token")) {
            authToken =jsonMap.get("auth_token").toString().split(" ")[1];
            errorMessage="\n";
        }

        Assertions.assertNull(authToken, "Не валидные регистрационные данные" +
                "прошли проверку." + errorMessage);
    }

    @Test
    public void testJsonNode () throws IOException {
        String errorMessage = "";
        /*var testRegisterRequest = new TestRegisterRequest(
                "jsonnode1",
                "jsonnode@yandex.ru",
                "verys1Mpepassword"
        );*/

        var testRegisterRequest=new TestRegisterRequest(
                testUser.getLogin(),
                testUser.getEmail(),
                testUser.getHashPassword()
        );

        JsonNode jsonResponse = sendPOSTRequestTree(urlRegistration, testRegisterRequest, null);

        if (jsonResponse.has("message")) {
            errorMessage="\n"+jsonResponse.get("message").toString();
        }

        System.out.println(jsonResponse.toString());
        System.out.println(errorMessage);


    }

    @BeforeAll
    public static void deleteTestUser() {

    }

    public static void main(String[] args) throws IOException {
    }
}
