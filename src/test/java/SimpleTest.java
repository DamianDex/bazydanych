import entity.Categories;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.Test;
import tables.categories.CategoriesOperations;

import javax.persistence.Query;

public class SimpleTest {

    @Test
    public void addCategory() {
        SessionFactory sessionFactory = new Configuration().configure()
                .buildSessionFactory();
        Session session = sessionFactory.openSession();
        CategoriesOperations.getCategoryByName(session, "Kategoria");
        session.close();
    }
}
