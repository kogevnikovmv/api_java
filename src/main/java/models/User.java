package models;

import jakarta.persistence.*;


@Entity
@Table(name = "users")
public class User {

    private int id;
    private String login;
    private String hashPassword;
    private String email;

    public User() {}

    public User(String login, String email, String hashPassword) {
        this.login = login;
        this.email = email;
        this.hashPassword = hashPassword;
    }

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "login")
    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    @Column(name = "password")
    public String getHashPassword() {
        return hashPassword;
    }

    public void setHashPassword(String hashPassword) {
        this.hashPassword = hashPassword;
    }

    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
