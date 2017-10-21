package utils;

import entity.Categories;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class SessionCreator {

    private static Session session;

    public static Session getSession() {
        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        session = sessionFactory.openSession();

        return session;
    }
}
