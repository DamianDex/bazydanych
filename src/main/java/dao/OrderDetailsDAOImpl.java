package dao;

import entities.OrderDetails;
import entities.keys.OrderDetailsKey;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import java.util.List;

public class OrderDetailsDAOImpl implements OrderDetailsDAO {

    private static final Logger LOGGER = LoggerFactory.logger(CategoriesDAOImpl.class);
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public OrderDetails getOrderDetailsByOrderId() {
        Session session = prepareSession();
        OrderDetails orderDetails = session.load(OrderDetails.class, new OrderDetailsKey(10248, 11));
        LOGGER.info("Order loaded successfully, Order details = " + orderDetails);
        finishSession(session);
        return orderDetails;
    }

    public List<OrderDetails> listOrderDetails() {
        Session session = prepareSession();
        List<OrderDetails> orderDetailsList = session.createQuery("from OrderDetails").list();
        for (OrderDetails orderDetails : orderDetailsList) {
            LOGGER.info("Order Details List: " + orderDetails);
        }
        finishSession(session);
        return orderDetailsList;
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
