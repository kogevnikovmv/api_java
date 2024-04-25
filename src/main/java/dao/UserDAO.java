package dao;

import models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import utils.HibernateSessionFactory;

import java.util.List;

public class UserDAO {
    public User findById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, id);
    }

    public User findByLogin(String login) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, login);
    }

    public void save(User user) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.persist(user);
        transaction.commit();
        session.close();
    }

    public void update(User user) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.merge(user);
    }

    public void delete(User user) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.remove(user);
        transaction.commit();
        session.close();
    }

    public List<User> findAllUsers() {
        List<User> users= (List<User>) HibernateSessionFactory.getSessionFactory().openSession().createQuery("FROM Users").list();
        return users;
    }
}
