package service;

import dao.CustomersDAO;
import entities.Customers;

import java.util.List;

public class CustomersServiceImpl implements CustomersService {

    private CustomersDAO customersDAO;

    public void setCustomersDAO(CustomersDAO customersDAO) {
        this.customersDAO = customersDAO;
    }

    public void addCustomer(Customers customers) {
        customersDAO.addCustomer(customers);
    }

    public void updateCustomer(Customers customers) {
        customersDAO.updateCustomer(customers);
    }

    public List<Customers> listCustomers() {
        return customersDAO.listCustomers();
    }

    public Customers getCustomerById(String id) {
        return customersDAO.getCustomerById(id);
    }

    public void removeCustomerById(String id) {
        customersDAO.removeCustomerById(id);
    }
}
