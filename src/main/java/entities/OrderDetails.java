package entities;

import entities.keys.OrderDetailsKey;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "order_details")
@AssociationOverrides({
        @AssociationOverride(name = "pk.orders",
                joinColumns = @JoinColumn(name = "orderid")),
        @AssociationOverride(name = "pk.products",
                joinColumns = @JoinColumn(name = "productid")) })
public class OrderDetails implements Serializable {


    private OrderDetailsId pk = new OrderDetailsId();
    private double unitprice;
    private int quantity;
    private double discount;

    public OrderDetails() {
    }

    public OrderDetails(double unitprice, int quantity, double discount) {

        this.unitprice = unitprice;
        this.quantity = quantity;
        this.discount = discount;
    }

    @EmbeddedId
    public OrderDetailsId getPk() {
        return pk;
    }

    public void setPk(OrderDetailsId pk) {
        this.pk = pk;
    }

    @Transient
    public Orders getOrders(){
        return getPk().getOrders();
    }

    public void setOrders(Orders order){
        getPk().setOrders(order);
    }

    @Transient
    public Products getProducts(){
        return getPk().getProducts();
    }

    public void setProducts(Products products){
        getPk().setProducts(products);
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
                "unitprice=" + unitprice +
                ", quantity=" + quantity +
                ", discount=" + discount +
                '}';
    }
}
