package ru.appapi.services;

import ru.appapi.dao.UserDAO;
import ru.appapi.models.Token;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import ru.appapi.models.User;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    private UserDAO userDao= new UserDAO();
    private static UserService userService;

    public static UserService getUserService() {
        if (userService==null) {
            UserService userService= new UserService();
        }
        return userService;
    }

    public User findUserById(int id) {
        return userDao.findById(id);
    }

    public User findUserByLogin(String login) {
        return userDao.findByLogin(login);
    }
    public User findUserByEmail(String email) {
        return userDao.findByEmail(email);
    }

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public List<String> getAllTokens() {
        return userDao.getAllTokens().stream()
                .map(Token::getTokenValue)
                .collect(Collectors.toList());
    }

    //save user and create token
    public String saveUser(User user) {
        Token token=createToken(user);
        user.setToken(token);
        token.setUser(user);
        userDao.save(user);
        return token.getTokenValue();
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public String changePassword(String token, String newPassword) {
        User user=validateUserByToken(token);
        if (user!=null) {
            user.setHashPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));

            Token newToken=createToken();
            user.setToken(newToken);
            newToken.setUser(user);

            userDao.update(user);
            return newToken.getTokenValue();
        }
        else {
            return null;
        }

    }

    public User validateUserByToken(String token) {
        return userDao.findByToken(token);
    }

    public boolean validateUserByLogin(User user, String candidatePassword) {
        return BCrypt.checkpw(candidatePassword, user.getHashPassword());
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }

    public Token createToken(User user) {
        return new Token(user, UUID.randomUUID());
    }

    public Token createToken() {
        return new Token(UUID.randomUUID());
    }

}
