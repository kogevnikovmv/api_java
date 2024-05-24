package testModels;
import models.LoginRequest;

public class TestLoginRequest extends LoginRequest{

    private String login;
    private String password;

    public TestLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public String getPassword() {
        return password;
    }

}
