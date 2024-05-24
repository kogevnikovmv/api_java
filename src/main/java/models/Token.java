package models;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {

    private int id;
    private String tokenValue;
    private User user;

    public Token() {
    }

    public Token(User user, String tokenValue) {
        this.user = user;
        this.tokenValue = tokenValue;
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

    @Column(name = "token")
    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @OneToOne
    @JoinColumn(name = "user_id")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}