package models;

import jakarta.persistence.*;

@Entity
@Table(name = "tokens")
public class Token {

    private int id;
    private String token;
    private User user;

    public Token() {
    }

    public Token(User user, String token) {
        this.user = user;
        this.token = token;
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
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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