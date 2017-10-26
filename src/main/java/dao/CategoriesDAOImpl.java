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
        Session session = this.sessionFactory.getCurrentSession();
        session.persist(categories);
        LOGGER.info("Category saved successfully, Category Details = " + categories);
    }

    public void updateCategory(Categories categories) {
        Session session = this.sessionFactory.getCurrentSession();
        session.update(categories);
        LOGGER.info("Category updated successfully, Category Details = " + categories);
    }

    @SuppressWarnings("unchecked")
    public List<Categories> listCategories() {
        Session session = this.sessionFactory.getCurrentSession();
        List<Categories> categoriesList = session.createQuery("from Categories").list();
        for (Categories categories : categoriesList) {
            LOGGER.info("Categories List: " + categories);
        }
        return categoriesList;
    }

    public Categories getCategoryById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Categories categories = session.load(Categories.class, new Integer(id));
        LOGGER.info("Category loaded successfully, Category details = " + categories);
        return categories;
    }

    public void removeCategoryById(int id) {
        Session session = this.sessionFactory.getCurrentSession();
        Categories categories = session.load(Categories.class, new Integer(id));
        if (categories != null) {
            session.delete(categories);
        }
        LOGGER.info("Category deleted successfully, Category details = " + categories);
    }
}
