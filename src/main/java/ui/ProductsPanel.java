package ui;

import entities.Categories;
import entities.Products;
import helpers.ServiceHelper;
import service.CategoriesServiceImpl;
import service.ProductsServiceImpl;
import ui.custom.PlaceholderTextField;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ProductsPanel extends JPanel {
    private JPanel productsPanel;
    private JTable productsTable;
    private JTextField productNameTextField;
    private JTextField supplierIdTextField;
    private JTextField quantityPerUnitTextField;
    private JTextField unitPriceTextField;
    private JTextField unitsInStockTextField;
    private JTextField unitsOnOrderTextField;
    private JTextField reorderLevelTextField;
    private JTextField discontinuedTextField;
    private JComboBox categoriesIdComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextField minPriceTextField;
    private JTextField maxPriceTextField;
    private JButton searchButton;
    private JButton readAllButton;

    private ProductsServiceImpl productsService = new ServiceHelper().getProductsService();
    private CategoriesServiceImpl categoriesService = new ServiceHelper().getCategoriesService();

    private static final String[] COLUMN_HEADINGS = {"Product Id","Product Name", "Categoryid",
                                                    "SupplierId", "Quantity Per Unit", "Unit Price",
                                                    "Units In Stock", "Units On Order", "Reorder Level", "Discontinued"};
    private static final int INITIAL_ROW_NUMBER = 0;

    private Products selectedProduct;

    public ProductsPanel(){
        initUi();
        addAllActionListeners();
    }

    private void addAllActionListeners() {
        addActionListenerToReadAllBtn();
       // addActionListenerToTableRowSelection();
        //addActionListenerToUpdateBtn();
    }


    private void addActionListenerToReadAllBtn() {
        readAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                pushDataFromDbToTable();
            }
        });
    }
//
//    private void addActionListenerToUpdateBtn() {
//        updateButton.addActionListener(new ActionListener() {
//            public void actionPerformed(ActionEvent actionEvent) {
//                selectedCategory.setCategoryId(Integer.parseInt(categoryId.getText()));
//                selectedCategory.setCategoryName(categoryName.getText());
//                selectedCategory.setDescription(categoryDescription.getText());
//                categoriesService.updateCategory(selectedCategory);
//                pushDataFromDbToTable();
//            }
//        });
//    }
//    private void addActionListenerToTableRowSelection() {
//        ListSelectionModel cellSelectionModel = categoriesTable.getSelectionModel();
//        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
//
//        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
//            public void valueChanged(ListSelectionEvent e) {
//                try {
//                    Integer id = Integer.valueOf(categoriesTable.getValueAt(categoriesTable.getSelectedRow(), 0).toString());
//                    selectedCategory = categoriesService.getCategoryById(id);
//                    categoryId.setText(String.valueOf(selectedCategory.getCategoryId()));
//                    categoryName.setText(selectedCategory.getCategoryName());
//                    categoryDescription.setText(selectedCategory.getDescription());
//                } catch (Exception ex) {
//                    //do nothing
//                }
//            }
//        });
//    }

    private void initUi(){
        initEmptyProductsTable();
    }

    private void initEmptyProductsTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ROW_NUMBER, COLUMN_HEADINGS.length);
        model.setColumnIdentifiers(COLUMN_HEADINGS);
        productsTable.setModel(model);
        productsTable.setRowSelectionAllowed(true);
        productsTable.setCellSelectionEnabled(false);
    }

    private void pushDataFromDbToTable() {
        List<Products> productsList = productsService.listProducts();
        DefaultTableModel tableModel = (DefaultTableModel) productsTable.getModel();
        tableModel.setRowCount(0); //clear JTable

        for (Products product : productsList) {
            tableModel.addRow(product.toArray());
        }
    }

}
