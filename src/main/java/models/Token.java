package models;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {

    private int id;
    private String tokenValue;
    private models.User user;

    public Token() {
    }

    public Token(models.User user, String tokenValue) {
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

    @Column(name = "tokenValue")
    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    @OneToOne(mappedBy = "token")
    public models.User getUser() {
        return user;
    }

    public void setUser(models.User user) {
        this.user = user;
    }
}