package ui.custom;

import entities.OrderDetails;
import entities.Products;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class OrdersAbstractTableModel extends AbstractTableModel {

    private final String[] COLUMN_NAMES = {"Product Name", "Unit Price", "Discount", "Quantity",
            "Total","Actions"};
    private List<String> productsNameList;
    private List<OrderDetails> orderDetailsList;


    public OrdersAbstractTableModel() {
        productsNameList = new ArrayList<>();
        orderDetailsList = new ArrayList<>();
    }

    public String[] getHeaders(){
        return COLUMN_NAMES;
    }

    public String[] getLine(int row){
        OrderDetails od = orderDetailsList.get(row);
        String[] dataRow = {productsNameList.get(row),
                String.valueOf((double) Math.round(od.getUnitprice()*100)/100),
                String.valueOf((double) Math.round(od.getDiscount()*100)/100),
                String.valueOf((double) Math.round((double)od.getQuantity()*100)/100),
                String.valueOf((double) Math.round((double)getTotalPrice(od.getUnitprice(), od.getQuantity(), od.getDiscount())*100)/100)};
        return dataRow;
    }

    public void addNewOrder(Products product){
        productsNameList.add(product.getProductname());
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setProductId(product);
        orderDetails.setUnitprice(product.getUnitprice());
        orderDetails.setQuantity(1);
        orderDetails.setDiscount(0);
        orderDetailsList.add(orderDetails);
        fireTableDataChanged();
        fireTableChanged(new TotalPriceChangedTableModelEvent(this));
    }

    public void removeOrder(int row){
        productsNameList.remove(row);
        orderDetailsList.remove(row);
        fireTableChanged(new TotalPriceChangedTableModelEvent(this));
    }

    @Override
    public Class getColumnClass(int c) {
        switch (c) {
            case 1:
            case 2:
            case 4:
                return Double.class;
            case 3:
                return Integer.class;
            case 0:
                return String.class;
            case 5:
                return JButton.class;
            default:
                return String.class;
        }
    }



    @Override
    public boolean isCellEditable(int r, int c) {
        return (c == 5 || c == 2 || c == 3);
    }

    @Override
    public String getColumnName(int c) {
        return COLUMN_NAMES[c];
    }

    @Override
    public int getRowCount() {
        return orderDetailsList.size();
    }

    @Override
    public int getColumnCount() {
        return 6;
    }

    @Override
    public Object getValueAt(int r, int c) {
        OrderDetails orderDetails =orderDetailsList.get(r);
        switch (c) {
            case 0:
                return productsNameList.get(r);
            case 1:
                return orderDetails.getUnitprice();
            case 2:
                return orderDetails.getDiscount();
            case 3:
                return orderDetails.getQuantity();
            case 4:
                return getTotalPrice(orderDetails.getUnitprice(),orderDetails.getQuantity(), orderDetails.getDiscount());
            default:
                return null;
        }

    }

    @Override
    public void setValueAt(Object value, int r, int c) {
        OrderDetails orderDetails = orderDetailsList.get(r);
        switch (c) {
            case 2:
                orderDetails.setDiscount((Double) value);
                break;
            case 3:
                orderDetails.setQuantity((Integer) value);
                break;
            case 5:
                this.fireTableChanged(new OrdersButtonTableModelEvent(this, r));
                break;
            default:
                break;
        }
        if (c == 2 || c == 3){
            fireTableCellUpdated(r, 4);
            this.fireTableChanged(new TotalPriceChangedTableModelEvent(this));
        }
    }

    private double getTotalPrice(double unitPrice, double quantity, double discount){
        return unitPrice*quantity - unitPrice*quantity*discount;
    }

}
