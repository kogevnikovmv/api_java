package ru.appapi.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import org.mindrot.jbcrypt.BCrypt;

import jakarta.validation.constraints.Email;


@Entity
@Table(name = "users")
public class User {

    private int id;
    @Pattern(regexp="^[a-zA-Z0-9_-]{2,16}$")
    private String login;
    @Pattern(regexp="")
    private String hashPassword;
    @Email
    private String email;


    private Token token;

    public User() {}

    public User(String login, String email, String hashPassword) {
        this.login = login;
        this.email = email;
        //this.hashPassword = hashPassword;
        this.hashPassword= BCrypt.hashpw(hashPassword, BCrypt.gensalt(12));
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

    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="token_id")
    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

}
