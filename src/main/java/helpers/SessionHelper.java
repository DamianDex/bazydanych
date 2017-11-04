package helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

public class SessionHelper {

    private SessionFactory sessionFactory;

    public SessionHelper(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void finishSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }

    public Session prepareSession() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        return session;
    }
}
