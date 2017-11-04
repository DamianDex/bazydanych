package service;

import entities.Customers;

import java.util.List;

public interface CustomersService {

    public void addCustomer(Customers customers);

    public void updateCustomer(Customers customers);

    public List<Customers> listCustomers();

    public Customers getCustomerById(String id);

    public void removeCustomerById(String id);
}
