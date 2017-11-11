package dao;

import entities.OrderDetails;
import entities.Orders;
import entities.Products;

import java.util.List;

public interface OrderDetailsDAO {
    public OrderDetails getOrderDetailsByOrderId();

    public List<OrderDetails> listOrderDetails();

}
