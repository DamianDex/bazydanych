package ui.custom;

import entities.Products;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProductsAbstractTableModel extends AbstractTableModel {

    private final String[] COLUMN_NAMES = {"Product ID", "Product Name", "Quantity Per Unit", "Unit Price", "Units In Stock",
            "Units On Order", "Actions"};
    private List<Products> productsList;
    private List<Products> filteredProductsList;


    public ProductsAbstractTableModel() {
        productsList = new ArrayList<>();
        filteredProductsList = new ArrayList<>();
    }

    public boolean isProductsListEmpty() {
        return productsList.isEmpty();
    }

    public void showAllProducts() {
        filteredProductsList.clear();
        filteredProductsList.addAll(productsList);
        fireTableDataChanged();
    }

    public void setProductsList(List<Products> productsList) {
        this.productsList.addAll(productsList);
        this.filteredProductsList.addAll(productsList);
        fireTableDataChanged();
    }

    public void filterProductsList(String name) {
        filteredProductsList = productsList.stream().filter(x -> (x.getProductname().length() >= name.length() &&
                x.getProductname().toLowerCase().substring(0, name.length())
                        .equals(name.toLowerCase()))).collect(Collectors.toList());
        fireTableDataChanged();
    }

    @Override
    public Class getColumnClass(int c) {
        switch (c) {
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
        return filteredProductsList.size();
    }

    @Override
    public int getColumnCount() {
        return 7;
    }

    @Override
    public Object getValueAt(int r, int c) {
        Products product = filteredProductsList.get(r);
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
        Products product = filteredProductsList.get(r);
        switch (c) {
            case 6:
                this.fireTableChanged(new ProductsButtonTableModelEvent(this, product));
                break;
            default:
                break;
        }
    }

}
