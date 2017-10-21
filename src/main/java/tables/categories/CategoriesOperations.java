package tables.categories;

import entity.Categories;
import org.hibernate.Session;
import utils.SessionCreator;

import javax.persistence.Query;

public class CategoriesOperations {

    public static void getCategoryByName(Session session, String name) {
        session.beginTransaction();
        Query query = session.createQuery(String.format("from Categories C where C.categoryName = '%s'", name));
        for (Object categories : query.getResultList()) {
            System.out.println(((Categories) categories).getDescription() + "\n");
        }
        session.getTransaction().commit();
    }

    public static void getAllCategories(Session session) {
        session.beginTransaction();
        Query query = session.createQuery("from Categories");
        for (Object categories : query.getResultList()) {
            System.out.println(((Categories) categories).getDescription() + "\n");
        }
        session.getTransaction().commit();
    }
}
