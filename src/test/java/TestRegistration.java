import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.commons.io.IOUtils;
import testModels.TestUser;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class TestRegistration {
    static String urlRegistration ="http://127.0.0.1:8080/user/register";
    static String urlLogin="http://127.0.0.1:8080/user/login";
    static TestUser testUser;
    static ObjectMapper objectMapper = new ObjectMapper();

    static HttpResponse sendPOSTRequest(String url, String json) throws IOException {
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpPost postRequest= new HttpPost(url);
        StringEntity postBody = new StringEntity(json);
        postRequest.setEntity(postBody);
        postRequest.setHeader("Content-type", "application/json");
        return httpClient.execute(postRequest);
    }
    static void registerTestUser() throws IOException {
        String json = objectMapper.writeValueAsString(testUser);
        System.out.println(json);
        HttpResponse response = sendPOSTRequest(urlRegistration, json);

        String jsonResponse = IOUtils.toString(response.getEntity().getContent());

        System.out.println(jsonResponse);
    }
    public void loginTestUser() {    }
    public void deleteTestUser() {}

    public static void main(String[] args) throws IOException {
        testUser =new TestUser(
                "qwerty1",
                "qwerty1@yandex.ru",
                "simplepassword1"
        );
        registerTestUser();

    }
}
