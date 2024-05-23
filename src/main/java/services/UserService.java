package services;

import dao.UserDAO;
import models.Token;
import models.User;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public List<String> getAllTokens() {
        return userDao.getAllTokens().stream()
                .map(Token::getToken)
                .collect(Collectors.toList());
    }

    //save user and create token
    public String saveUser(User user) {
        Token token=new Token(user, UUID.randomUUID()
                .toString()
                .replace("-", ""));
        user.setToken(token);
        token.setUser(user);
        userDao.save(user);
        //userDao.save(token);
        return token.getToken();
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public boolean changePassword(String token, String newPassword) {
        User user=validateUserByToken(token);
        if (user!=null) {
            user.setHashPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
            userDao.update(user);
            return true;
        }
        else {
            return false;
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

}
