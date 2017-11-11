package dao;

import entities.Orders;

import java.util.List;

public interface OrdersDAO {

    public Orders getOrderById(String id);
    public List<Integer> reportByYear(String year);
}
