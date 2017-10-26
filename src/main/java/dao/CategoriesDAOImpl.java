package dao;

import entities.Categories;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import java.util.List;

public class CategoriesDAOImpl implements CategoriesDAO {

    private static final Logger LOGGER = LoggerFactory.logger(CategoriesDAOImpl.class);
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addCategory(Categories categories) {
        Session session = prepareSession();
        session.save(categories);
        LOGGER.info("Category saved successfully, Category Details = " + categories);
        finishSession(session);
    }

    public void updateCategory(Categories categories) {
        Session session = prepareSession();
        session.update(categories);
        LOGGER.info("Category updated successfully, Category Details = " + categories);
        finishSession(session);
    }

    @SuppressWarnings("unchecked")
    public List<Categories> listCategories() {
        Session session = prepareSession();
        List<Categories> categoriesList = session.createQuery("from Categories").list();
        for (Categories categories : categoriesList) {
            LOGGER.info("Categories List: " + categories);
        }
        finishSession(session);
        return categoriesList;
    }

    public Categories getCategoryById(int id) {
        Session session = prepareSession();
        Categories categories = session.load(Categories.class, new Integer(id));
        LOGGER.info("Category loaded successfully, Category details = " + categories);
        finishSession(session);
        return categories;
    }

    public void removeCategoryById(int id) {
        Session session = prepareSession();
        Categories categories = session.load(Categories.class, new Integer(id));
        if (categories != null) {
            session.delete(categories);
        }
        LOGGER.info("Category deleted successfully, Category details = " + categories);
        finishSession(session);
    }

    private void finishSession(Session session) {
        session.getTransaction().commit();
        session.close();
    }

    private Session prepareSession() {
        Session session = this.sessionFactory.openSession();
        session.beginTransaction();
        return session;
    }
}
