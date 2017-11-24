package service;

import entities.Orders;
import org.hibernate.Session;

public interface OrdersService {
    void addOrder(Orders order);

}
