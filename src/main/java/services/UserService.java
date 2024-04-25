package services;

import dao.UserDAO;
import models.User;

import java.util.List;

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

    public void saveUser(User user) {
        userDao.save(user);
    }

    public void updateUser(User user) {
        userDao.update(user);
    }

    public void deleteUser(User user) {
        userDao.delete(user);
    }

    public List<User> findAllUsers() {
        return userDao.findAllUsers();
    }
}
