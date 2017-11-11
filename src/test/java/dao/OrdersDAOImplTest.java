package dao;

import entities.Categories;
import entities.Orders;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Order;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class OrdersDAOImplTest {
    @Test
    public void setSessionFactory() throws Exception {
    }

    private OrdersDAOImpl objectUnderTest;

    @Before
    public void setUp() {
        objectUnderTest = new OrdersDAOImpl();
        SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
        objectUnderTest.setSessionFactory(sessionFactory);
    }

    @After
    public void tearDown() {
        //do nothing
    }

    @Test
    public void getOrderById() throws Exception {
        Orders order = objectUnderTest.getOrderById("10248");
    }

    @Test
    public void reportByYear() throws Exception {
        objectUnderTest.reportByYear("1991");
    }
}