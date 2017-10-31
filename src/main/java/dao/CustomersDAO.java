package dao;

import entities.Customers;

import java.util.List;

public interface CustomersDAO {

    public void addCustomer(Customers customers);

    public void updateCustomer(Customers customers);

    public List<Customers> listCustomers();

    public Customers getCustomerById(String id);

    public void removeCustomerById(String id);
}
