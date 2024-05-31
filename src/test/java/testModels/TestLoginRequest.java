package testModels;
import testModels.Request;

public class TestLoginRequest extends Request{

    private String login;
    private String password;

    public TestLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getLogin() {
        return login;
    }


    public String getPassword() {
        return password;
    }

}
