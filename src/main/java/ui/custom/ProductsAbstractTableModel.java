package ui.custom;

import entities.Products;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

public class ProductsAbstractTableModel extends AbstractTableModel {

    private final String[] COLUMN_NAMES = {"Product ID", "Product Name", "Quantity Per Unit", "Unit Price", "Units In Stock",
                                    "Units On Order", "Actions"};
    private List<Products> productsList;


    public ProductsAbstractTableModel() {
        productsList = new ArrayList<>();
    }

    public List<Products> getProductsList() {
        return productsList;
    }

    public void setProductsList(List<Products> productsList) {
        this.productsList = productsList;
        fireTableDataChanged();
    }

    @Override
    public Class getColumnClass(int c) {
        switch(c){
            case 0:
            case 4:
            case 5:
                return Integer.class;
            case 3:
                return Double.class;
            case 6:
                return JButton.class;
            default:
                return String.class;
        }
    }

    @Override
    public boolean isCellEditable(int r, int c) {
        return c == 6;
    }

    @Override
    public String getColumnName(int c) {
        return COLUMN_NAMES[c];
    }

    @Override
    public int getRowCount() {
        return productsList.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int r, int c) {
        Products product = productsList.get(r);
        switch (c) {
            case 0:
                return product.getProductid();
            case 1:
                return product.getProductname();
            case 2:
                return product.getQuantityperunit();
            case 3:
                return product.getUnitprice();
            case 4:
                return product.getUnitsinstock();
            case 5:
                return product.getUnitsonorder();
            default:
                return null;
        }

    }

    @Override
    public void setValueAt(Object value, int r, int c) {
        Products product = productsList.get(r);
        switch (c) {
            case 6:
                this.fireTableChanged(new ProductsButtonTableModelEvent(this, product));
                break;
            default:
                break;
        }
    }

}
