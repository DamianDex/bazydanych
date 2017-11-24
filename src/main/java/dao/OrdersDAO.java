package dao;


import entities.Orders;
import org.hibernate.Session;

public interface OrdersDAO {

    public void addOrder(Orders order);
}
