package entities;

import entities.keys.OrderDetailsKey;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_details")
@IdClass(OrderDetailsKey.class)
public class OrderDetails implements Serializable {

    @Id
    @ManyToOne
    @JoinColumn(name = "orderId")
    private Orders orderId;

    @Id
    @ManyToOne
    @JoinColumn(name = "productid")
    private Products productId;

    private double unitprice;
    private int quantity;
    private double discount;

    public OrderDetails() {
    }

    public OrderDetails(Orders orderId, Products productId, double unitprice, int quantity, double discount) {
        this.orderId = orderId;
        this.productId = productId;
        this.unitprice = unitprice;
        this.quantity = quantity;
        this.discount = discount;
    }

    public Orders getOrderId() {
        return orderId;
    }

    public void setOrderId(Orders orderId) {
        this.orderId = orderId;
    }

    public Products getProductId() {
        return productId;
    }

    public void setProductId(Products productId) {
        this.productId = productId;
    }

    public double getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(double unitprice) {
        this.unitprice = unitprice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getDiscount() {
        return discount;
    }

    public void setDiscount(double discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "OrderDetails{" +
                "orderId=" + orderId +
                ", productId=" + productId +
                ", unitprice=" + unitprice +
                ", quantity=" + quantity +
                ", discount=" + discount +
                '}';
    }
}
