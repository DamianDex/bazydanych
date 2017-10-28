package dao;

import entities.Products;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ProductsDAOImpl implements ProductsDAO {

    private static final Logger LOGGER = LoggerFactory.logger(ProductsDAOImpl.class);
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void addProduct(Products products) {
        Session session = prepareSession();
        session.save(products);
        LOGGER.info("Products saved successfully, Products Details = " + products);
        finishSession(session);
    }

    public void updateProduct(Products products) {
        Session session = prepareSession();
        session.update(products);
        LOGGER.info("Products updated successfully, Products Details = " + products);
        finishSession(session);
    }

    public List<Object> listProductsWithCategories(double minPrice, double maxPrice) {
        Session session = prepareSession();
        Query query = session.createQuery("from Products p inner join p.categories where unitprice > :minPrice " +
                "and unitprice < :maxPrice");
        query.setParameter("minPrice", minPrice);
        query.setParameter("maxPrice", maxPrice);
        List<Object> products = query.list();
        for (Object product : products) {
            LOGGER.info("Products list: " + product);
        }
        session.close();
        return products;
    }

    public Products getProductById(int productid) {
        Session session = prepareSession();
        Products products = session.load(Products.class, new Integer(productid));
        LOGGER.info("Products loaded successfully, Products details = " + products);
        finishSession(session);
        return products;
    }

    public void removeProductById(int productid) {
        Session session = prepareSession();
        Products products = session.load(Products.class, new Integer(productid));
        if (products != null){
            session.delete(products);
        }
        LOGGER.info("Products deleted successfully, Products details = " + products);
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
