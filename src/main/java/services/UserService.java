package services;

import dao.UserDAO;
import models.Token;
import models.User;

import java.util.List;
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

    public String saveUser(User user) {
        //выглядит отвратно
        user.setHashPassword(BCrypt.hashpw(user.getHashPassword(), BCrypt.gensalt(12)));

        return userDao.save(user);
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public User changePassword(String token, String newPassword) {
        User user=userDao.findByToken(token);
        user.setHashPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt(12)));
        return userDao.update(user);
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }

}
