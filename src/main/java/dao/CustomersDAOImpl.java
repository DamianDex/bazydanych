package dao;

import entities.Customers;
import helpers.SessionHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;

import java.util.List;

public class CustomersDAOImpl implements CustomersDAO {

    private static final Logger LOGGER = LoggerFactory.logger(CategoriesDAOImpl.class);
    private final SessionHelper sessionHelper;

    public CustomersDAOImpl(SessionFactory sessionFactory) {
        sessionHelper = new SessionHelper(sessionFactory);
    }

    public void addCustomer(Customers customers) {
        Session session = sessionHelper.prepareSession();
        session.save(customers);
        LOGGER.info("Customer saved successfully, Customer Details = " + customers);
        sessionHelper.finishSession(session);
    }

    public void updateCustomer(Customers customers) {
        Session session = sessionHelper.prepareSession();
        session.update(customers);
        LOGGER.info("Customer updated successfully, Customer Details = " + customers);
        sessionHelper.finishSession(session);
    }

    public List<Customers> listCustomers() {
        Session session = sessionHelper.prepareSession();
        List<Customers> customersList = session.createQuery("from Customers").list();
        for (Customers customers : customersList) {
            LOGGER.info("Customers List: " + customers);
        }
        sessionHelper.finishSession(session);
        return customersList;
    }

    public Customers getCustomerById(String id) {
        Session session = sessionHelper.prepareSession();
        Customers customer = session.load(Customers.class, id);
        LOGGER.info("Customer loaded successfully, Customer details = " + customer);
        sessionHelper.finishSession(session);
        return customer;
    }

    public void removeCustomerById(String id) {
        Session session = sessionHelper.prepareSession();
        Customers customer = session.load(Customers.class, id);
        if (customer != null) {
            session.delete(customer);
        }
        LOGGER.info("Customer deleted successfully, Customer details = " + customer);
        sessionHelper.finishSession(session);
    }
}
