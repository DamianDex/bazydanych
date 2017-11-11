package dao;

import entities.OrderDetails;
import entities.Orders;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrderDetailsDAOImplTest {
    private OrderDetailsDAOImpl objectUnderTest;

    @Before
    public void setUp() {
        objectUnderTest = new OrderDetailsDAOImpl();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        objectUnderTest.setSessionFactory(sessionFactory);
    }

    @After
    public void tearDown() {
        //do nothing
    }

    @Test
    public void getOrderDetailsByOrderId() throws Exception {
        OrderDetails orderDetails = objectUnderTest.getOrderDetailsByOrderId();
    }

    @Test
    public void listOrderDetails() throws Exception {
        objectUnderTest.listOrderDetails();
    }

}