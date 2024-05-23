package testModels;
import models.User;

public class TestUser extends User {
    private String login;
    private String hashPassword;
    private String email;

    public TestUser(String login, String email, String hashPassword) {
        this.login=login;
        this.email=email;
        this.hashPassword=hashPassword;
    }

    @Override
    public String getLogin() {
        return login;
    }

    @Override
    public void setLogin(String login) {
        this.login = login;
    }

    @Override
    public String getHashPassword() {
        return hashPassword;
    }

    @Override
    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
    }
}
