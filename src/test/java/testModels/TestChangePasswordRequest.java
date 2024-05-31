package testModels;

public class TestChangePasswordRequest extends Request {

    private String password;

    public TestChangePasswordRequest(String newPassword) {
        this.password=newPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
