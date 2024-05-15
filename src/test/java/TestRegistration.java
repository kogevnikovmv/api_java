import org.json.JSONObject;
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

    static HttpResponse sendPOSTRequest(String url, JSONObject json) throws IOException {
        HttpClient httpClient=HttpClientBuilder.create().build();
        HttpPost postRequest= new HttpPost(url);
        StringEntity postBody = new StringEntity(json.toString());
        postRequest.setEntity(postBody);
        postRequest.setHeader("Content-type", "application/json");
        return httpClient.execute(postRequest);
    }
    static void registerTestUser() throws IOException {
        JSONObject json=new JSONObject();
        json.put("login", testUser.getLogin());
        json.put("email", testUser.getEmail());
        json.put("password", testUser.getPassword());
        HttpResponse response = sendPOSTRequest(urlRegistration, json);
        String token=response.getEntity().toString().split(" ")[1];
        System.out.println(response.getEntity().toString());
        System.out.println(token);
        //testUser.setToken();
    }
    public void loginTestUser() {    }
    public void deleteTestUser() {}

    public static void main(String[] args) throws IOException {
        TestUser testUser =new TestUser(
                "qwerty1",
                "qwerty1@yandex.ru",
                "simplepassword1"
        );
        registerTestUser();

    }
}
