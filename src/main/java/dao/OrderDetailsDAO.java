package dao;

import entities.OrderDetails;

import java.util.List;

public interface OrderDetailsDAO {
    public OrderDetails getOrderDetailsByOrderId();

    public List<OrderDetails> listOrderDetails();
}
