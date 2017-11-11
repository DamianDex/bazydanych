package dao;

import entities.Orders;
import entities.Products;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;

import java.util.List;

public class OrdersDAOImpl implements OrdersDAO {

    private static final Logger LOGGER = LoggerFactory.logger(CategoriesDAOImpl.class);
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Orders getOrderById(String id) {
        Session session = prepareSession();
        Orders order = session.load(Orders.class, new Integer(id));
        LOGGER.info("Order loaded successfully, Order details = " + order);
        finishSession(session);
        return order;
    }

    public List<Integer> reportByYear(String year) {
        Session session = prepareSession();
        Query query = session.createQuery("Select orderId from Orders o join OrderDetails od");




        List<Integer> productsList = query.list();
        for (Integer products : productsList) {
            LOGGER.info("Products List: " + products);
        }
        finishSession(session);
        return productsList;
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
