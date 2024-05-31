package utils;

import models.Token;
import models.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactory {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration().configure();
                configuration.addAnnotatedClass(User.class);
                configuration.addAnnotatedClass(Token.class);
                //StandardServiceRegistryBuilder builder= new StandardServiceRegistryBuilder()
                //        .applySettings(configuration.getProperties());
                //sessionFactory= configuration.buildSessionFactory(builder.build());
                sessionFactory= configuration.buildSessionFactory();

            } catch (Exception e) {
                System.out.println("error: "+e);
            }
        }
        //System.out.println(sessionFactory);
        return sessionFactory;
    }
}
