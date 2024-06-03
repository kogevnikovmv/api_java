package ru.appapi.models;

import jakarta.persistence.*;

import java.util.UUID;

@Entity
@Table(name = "tokens")
public class Token {

    private int id;
    private String tokenValue;
    private User user;

    public Token() {
    }

    public Token(User user, UUID tokenValue) {
        this.user = user;
        this.tokenValue = tokenValue.toString()
                .replace("-", "");
    }

    public Token(UUID tokenValue) {
        this.tokenValue = tokenValue.toString()
                .replace("-", "");
    }

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "tokenValue")
    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @OneToOne(mappedBy = "token")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}