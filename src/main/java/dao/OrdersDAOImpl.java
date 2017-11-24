package dao;

import entities.OrderDetails;
import entities.Orders;
import helpers.SessionHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

public class OrdersDAOImpl implements OrdersDAO {

    private static final Logger LOGGER = LoggerFactory.logger(OrdersDAOImpl.class);
    private SessionHelper sessionHelper;

    public OrdersDAOImpl(SessionFactory sessionFactory){
        this.sessionHelper = new SessionHelper(sessionFactory);
    }

    @Override
    public void addOrder(Orders order) {
        Session session = sessionHelper.prepareSession();
        session.save(order);
        LOGGER.info("Order saved successfully, Order = " + order);
        sessionHelper.finishSession(session);
    }
}
