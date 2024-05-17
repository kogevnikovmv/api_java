package testModels;
import models.LoginRequest;

public class TestLoginRequest extends LoginRequest{

    private final String login;
    private final String password;

    public TestLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }
}
