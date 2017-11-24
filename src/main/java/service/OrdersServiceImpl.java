package service;

import dao.OrdersDAO;
import entities.Orders;
import org.hibernate.Session;

public class OrdersServiceImpl implements OrdersService {


    OrdersDAO ordersDAO;

    public OrdersDAO getOrdersDAO() {
        return ordersDAO;
    }

    public void setOrdersDAO(OrdersDAO ordersDAO) {
        this.ordersDAO = ordersDAO;
    }

    @Override
    public void addOrder(Orders order) {
        ordersDAO.addOrder(order);
    }
}
