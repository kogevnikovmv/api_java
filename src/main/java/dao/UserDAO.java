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

    //не верно
    public User findByLogin(String login) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        List<User> result=session.createSelectionQuery("from User where login= :value", User.class)
                .setParameter("value", login)
                .getResultList();
        transaction.commit();
        session.close();
        return result.get(0); // =(
    }

    public User findByToken(String token) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        List<User> user=session.createSelectionQuery("from Token where token= :value", User.class)
                .setParameter("value", token)
                .getResultList();
        transaction.commit();
        session.close();
        return user.get(0); // =(
    }

    public List<User> getAllUsers() {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        List<User> result=session.createSelectionQuery("from User", User.class)
                .getResultList();
        transaction.commit();
        session.close();


        return result;
    }

    public User save(User user) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.persist(user);
        transaction.commit();
        session.close();
        return user; // нужно чтобы возвращал токен
    }

    public User update(User user) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.merge(user);
        transaction.commit();
        session.close();
        return user;
    }

    public void delete(User user) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.remove(user);
        transaction.commit();
        session.close();
    }
}
