package testModels;
import models.User;

public class TestUser extends User {

    public TestUser(String login, String email, String hashPassword) {
        super(login, email, hashPassword);
    }
}
