package ru.appapi.dao;

import ru.appapi.models.Token;
import ru.appapi.models.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import ru.appapi.utils.HibernateSessionFactory;

import java.util.List;

public class UserDAO {
    public User findById(int id) {
        return HibernateSessionFactory.getSessionFactory().openSession().get(User.class, id);
    }

    //не верно
    public User findByLogin(String login) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        List <User> result=session.createSelectionQuery("from User where login= :value", User.class)
                .setParameter("value", login)
                .getResultList();
        transaction.commit();
        session.close();
        if (result.isEmpty()) {return null;}
        else {return result.get(0);}
    }

    public User findByEmail(String email) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        List<User> result=session.createSelectionQuery("from User where email= :value", User.class)
                .setParameter("value", email)
                .getResultList();
        transaction.commit();
        session.close();
        if (result.isEmpty()) {return null;}
        else {return result.get(0);}
    }

    public User findByToken(String token) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        List<Token> userToken=session.createSelectionQuery("from Token where tokenValue= :value", Token.class)
                .setParameter("value", token)
                .getResultList();
        transaction.commit();
        session.close();

        return userToken.get(0).getUser(); // =(
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
        return user;
    }

    public Token save(Token token) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.persist(token);
        transaction.commit();
        session.close();
        return token;
    }

    public List<Token> getAllTokens() {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        List<Token> result=session.createSelectionQuery("from Token", Token.class)
                .getResultList();
        transaction.commit();
        session.close();


        return result;
    }

    public User update(User user) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.merge(user);
        transaction.commit();
        session.close();
        return user;
    }

    public Token update(Token token) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.merge(token);
        transaction.commit();
        session.close();
        return token;
    }

    public void delete(User user) {
        Session session= HibernateSessionFactory.getSessionFactory().openSession();
        Transaction transaction= session.beginTransaction();
        session.remove(user);
        transaction.commit();
        session.close();
    }
}
