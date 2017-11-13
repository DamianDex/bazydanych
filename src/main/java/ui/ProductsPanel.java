package ui;

import entities.Categories;
import entities.Products;
import entities.Suppliers;
import helpers.ServiceHelper;
import service.CategoriesServiceImpl;
import service.ProductsServiceImpl;
import service.SuppliersServiceImpl;
import ui.custom.PlaceholderTextField;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.*;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductsPanel extends JPanel {
    private static final String[] COLUMN_HEADINGS = {"Product Id", "Product Name", "Supplier Name",
            "Category Name", "Quantity Per Unit", "Unit Price", "Units In Stock",
            "Units On Stock", "Reorder Level", "Discontinued"};
    private static final int INITIAL_ROW_NUMBER = 0;
    private static final String PRODUCT_NUMBER = "Please insert a number";
    private JPanel productsPanel;
    private JPanel buttonPanel;
    private JPanel currentSelectionPanel;
    private JPanel productsDetailsPanel;
    private JButton addNewButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JTextField unitPriceTextField;
    private JTable productsTable;
    private JTextField quantityTextField;
    private JTextField discontinuedTextField;
    private JTextField productNameTextField;
    private JTextField unitsInStockTextField;
    private JTextField unitsOnOrderTextField;
    private JTextField reorderLevelTextField;
    private JComboBox categoryNameComboBox;
    private JComboBox supplierNameComboBox;
    private PlaceholderTextField minPricePlaceholderTextField;
    private PlaceholderTextField maxPricePlaceholderTextField;
    private JButton searchButton;
    private JPanel searchPanel;

    private ProductsServiceImpl productsService = new ServiceHelper().getProductsService();
    private CategoriesServiceImpl categoriesService = new ServiceHelper().getCategoriesService();
    private SuppliersServiceImpl suppliersService = new ServiceHelper().getSuppliersService();
    private Products selectedProduct;
    private Map<String, Categories> categoriesMap;
    private Map<String, Suppliers> suppliersMap;
    private JTextField[] tableNumberTextField = {unitPriceTextField, unitsInStockTextField, unitsOnOrderTextField,
            reorderLevelTextField, discontinuedTextField};


    public ProductsPanel() {
        initUi();
        addAllActionListeners();
    }

    private void addAllActionListeners() {
        addActionListenerToSearchBtn();
        addActionListenerToAddNewBtn();
        addActionListenerToUpdateBtn();
        addActionListenerToDeleteBtn();
        addActionListenerToTableRowSelection();
        addActionListenerToAllTextFields();
        addMouseListenerToProductsPanel();
    }

    private void addMouseListenerToProductsPanel() {
        productsPanel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                clearProductTextFields();
            }
        });
    }

    private void addActionListenerToAllTextFields() {
        for (JTextField jTextField : tableNumberTextField) {
            jTextField.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    if (Character.isLetter(e.getKeyChar())) {
                        showErrorDialog(PRODUCT_NUMBER);
                    }
                }
            });
        }
    }

    private void addActionListenerToTableRowSelection() {
        ListSelectionModel cellSelectionModel = productsTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try {
                    Integer productId = Integer.valueOf(productsTable.getValueAt(productsTable.getSelectedRow(), 0).toString());
                    selectedProduct = productsService.getProductById(productId);
                    setProductTextFields(selectedProduct);
                } catch (Exception ex) {
                    //do nothing
                }
            }
        });
    }

    private void addActionListenerToDeleteBtn() {
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                productsService.removeProductById(selectedProduct.getProductid());
                pushAllDataFromDbToTable();
            }
        });

    }

    private void addActionListenerToUpdateBtn() {
        updateButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                selectedProduct.setProductname(productNameTextField.getText());
                selectedProduct.setQuantityperunit(quantityTextField.getText());
                selectedProduct.setUnitprice(Double.parseDouble(unitPriceTextField.getText()));
                selectedProduct.setUnitsinstock(Integer.parseInt(unitsInStockTextField.getText()));
                selectedProduct.setUnitsonorder(Integer.parseInt(unitsOnOrderTextField.getText()));
                selectedProduct.setReorderlevel(Integer.parseInt(reorderLevelTextField.getText()));
                selectedProduct.setDiscontinued(Integer.parseInt(discontinuedTextField.getText()));
                selectedProduct.setCategories(categoriesMap.get(categoryNameComboBox.getSelectedItem()));
                selectedProduct.setSuppliers(suppliersMap.get(supplierNameComboBox.getSelectedItem()));
                productsService.updateProduct(selectedProduct);
                pushAllDataFromDbToTable();
            }
        });
    }

    private void addActionListenerToAddNewBtn() {
        addNewButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Products productToAdd = getProductFromTextFields();

                productsService.addProduct(productToAdd);
                pushAllDataFromDbToTable();
            }
        });
    }

    private void addActionListenerToSearchBtn() {
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (minPricePlaceholderTextField.getText().isEmpty() && maxPricePlaceholderTextField.getText().isEmpty()) {
                    pushAllDataFromDbToTable();
                } else {
                    pushChoiceDataFromDbtoTable();
                }
            }
        });
    }

    private void initUi() {
        initEmptyProductsTable();
        loadCategoriesData();
        loadSuppliersData();
    }

    private void initEmptyProductsTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ROW_NUMBER, COLUMN_HEADINGS.length);
        model.setColumnIdentifiers(COLUMN_HEADINGS);
        productsTable.setModel(model);
        productsTable.setRowSelectionAllowed(true);
        productsTable.setCellSelectionEnabled(false);
    }

    private void loadCategoriesData() {
        categoriesMap = new HashMap<>();
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) categoryNameComboBox.getModel();
        comboBoxModel.addElement("");
        for (Categories x : categoriesService.listCategories()) {
            categoriesMap.put(x.getCategoryName(), x);
            comboBoxModel.addElement(x.getCategoryName());
        }
    }

    private void loadSuppliersData() {
        suppliersMap = new HashMap<>();
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) supplierNameComboBox.getModel();
        comboBoxModel.addElement("");
        for (Suppliers x : suppliersService.listSuppliers()) {
            suppliersMap.put(x.getCompanyName(), x);
            comboBoxModel.addElement(x.getCompanyName());
        }
    }

    private void setProductTextFields(Products products) {
        productNameTextField.setText(products.getProductname());
        quantityTextField.setText(products.getQuantityperunit());
        unitPriceTextField.setText(Double.toString(products.getUnitprice()));
        unitsInStockTextField.setText(Integer.toString(products.getUnitsinstock()));
        unitsOnOrderTextField.setText(Integer.toString(products.getUnitsonorder()));
        reorderLevelTextField.setText(Integer.toString(products.getReorderlevel()));
        discontinuedTextField.setText(Integer.toString(products.getDiscontinued()));
        categoryNameComboBox.setSelectedItem(products.getCategories().getCategoryName());
        supplierNameComboBox.setSelectedItem(products.getSuppliers().getCompanyName());
    }

    private Products getProductFromTextFields() {
        Products productToAdd = new Products();
        productToAdd.setProductname(productNameTextField.getText());
        productToAdd.setQuantityperunit(quantityTextField.getText());
        productToAdd.setUnitprice(Double.parseDouble(unitPriceTextField.getText()));
        productToAdd.setUnitsinstock(Integer.parseInt(unitsInStockTextField.getText()));
        productToAdd.setUnitsonorder(Integer.parseInt(unitsOnOrderTextField.getText()));
        productToAdd.setReorderlevel(Integer.parseInt(reorderLevelTextField.getText()));
        productToAdd.setDiscontinued(Integer.parseInt(discontinuedTextField.getText()));
        productToAdd.setCategories(categoriesMap.get(categoryNameComboBox.getSelectedItem()));
        productToAdd.setSuppliers(suppliersMap.get(supplierNameComboBox.getSelectedItem()));

        return productToAdd;
    }

    private void clearProductTextFields() {
        productNameTextField.setText("");
        quantityTextField.setText("");
        unitPriceTextField.setText("");
        unitsInStockTextField.setText("");
        unitsOnOrderTextField.setText("");
        reorderLevelTextField.setText("");
        discontinuedTextField.setText("");
    }

    private void showErrorDialog(String errorMsg) {
        JOptionPane.showMessageDialog(new JFrame(), errorMsg, "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    private void pushAllDataFromDbToTable() {
        List<Products> productsList = productsService.listProducts();
        DefaultTableModel tableModel = (DefaultTableModel) productsTable.getModel();
        tableModel.setRowCount(0); //clear JTable

        sortProducts(productsList);

        for (Products product : productsList) {
            tableModel.addRow(product.toArray());
        }
    }

    private void pushChoiceDataFromDbtoTable() {
        List<Products> productsList;
        if (minPricePlaceholderTextField.getText().isEmpty()) {
            productsList = productsService.listProductsByPrice(0, Double.parseDouble(maxPricePlaceholderTextField.getText()));
        } else if (maxPricePlaceholderTextField.getText().isEmpty()) {
            productsList = productsService.listProductsByPrice(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.MAX_VALUE);
        } else
            productsList = productsService.listProductsByPrice(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.parseDouble(maxPricePlaceholderTextField.getText()));

        DefaultTableModel tableModel = (DefaultTableModel) productsTable.getModel();
        tableModel.setRowCount(0); //clear JTable

        sortProducts(productsList);

        for (Products product : productsList) {
            tableModel.addRow(product.toArray());
        }

    }

    private void sortProducts(List<Products> productsList) {
        productsList.sort(new Comparator<Products>() {
            @Override
            public int compare(Products p1, Products p2) {
                if (p1.getProductid() > p2.getProductid())
                    return 1;
                else
                    return -1;
            }
        });
    }

}
