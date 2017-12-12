package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import entities.Categories;
import entities.Products;
import entities.Suppliers;
import helpers.ServiceHelper;
import service.CategoriesServiceImpl;
import service.ProductsServiceImpl;
import service.SuppliersServiceImpl;
import ui.custom.PlaceholderTextField;

import javax.persistence.PersistenceException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private static final String PRODUCT_FIELDS = "Please fill all fields in Current Selection";
    private static final String PRODUCT_CANT_REMOVE = "Product can't remove. Product is still used by OrderDetails";
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
    private JComboBox categoryNameComboBox2;
    private JComboBox supplierNameComboBox2;

    private ProductsServiceImpl productsService = new ServiceHelper().getProductsService();
    private CategoriesServiceImpl categoriesService = new ServiceHelper().getCategoriesService();
    private SuppliersServiceImpl suppliersService = new ServiceHelper().getSuppliersService();
    private Products selectedProduct;

    private Map<String, Categories> categoriesMap;
    private Map<String, Suppliers> suppliersMap;
    private JTextField[] tableNumberTextField = {unitPriceTextField, unitsInStockTextField, unitsOnOrderTextField,
            reorderLevelTextField, discontinuedTextField, minPricePlaceholderTextField, maxPricePlaceholderTextField};

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
        addFocusListenerToSupplierNameComboBox(supplierNameComboBox);
        addFocusListenerToSupplierNameComboBox(supplierNameComboBox2);
        addFocusListenerToCategoriesNameComboBox(categoryNameComboBox);
        addFocusListenerToCategoriesNameComboBox(categoryNameComboBox2);
    }

    private void addFocusListenerToSupplierNameComboBox(JComboBox supplierNameComboBox) {
        supplierNameComboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (supplierNameComboBox.getItemCount() - 1 != suppliersService.listSuppliers().size()) {
                    supplierNameComboBox.removeAllItems();
                    DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) supplierNameComboBox.getModel();
                    comboBoxModel.addElement("");
                    for (Suppliers x : suppliersService.listSuppliers()) {
                        suppliersMap.put(x.getCompanyName(), x);
                        comboBoxModel.addElement(x.getCompanyName());
                    }
                }
            }
        });
    }

    private void addFocusListenerToCategoriesNameComboBox(JComboBox categoryNameComboBox) {
        categoryNameComboBox.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (categoryNameComboBox.getItemCount() - 1 != categoriesService.listCategories().size()) {
                    categoryNameComboBox.removeAllItems();
                    DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) categoryNameComboBox.getModel();
                    comboBoxModel.addElement("");
                    for (Categories x : categoriesService.listCategories()) {
                        categoriesMap.put(x.getCategoryName(), x);
                        comboBoxModel.addElement(x.getCategoryName());
                    }
                }
            }
        });
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
                        e.consume();
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
                try {
                    productsService.removeProductById(selectedProduct.getProductid());
                } catch (PersistenceException ex) {
                    showErrorDialog(PRODUCT_CANT_REMOVE);
                }
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
                try {
                    Products productToAdd = getProductFromTextFields();
                    productsService.addProduct(productToAdd);
                    pushAllDataFromDbToTable();
                    clearProductTextFields();
                } catch (NumberFormatException | NullPointerException ex) {
                    showErrorDialog(PRODUCT_FIELDS);
                }
            }
        });
    }

    private void addActionListenerToSearchBtn() {
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (minPricePlaceholderTextField.getText().isEmpty() && maxPricePlaceholderTextField.getText().isEmpty()
                        && categoryNameComboBox2.getSelectedItem().equals("") && supplierNameComboBox2.getSelectedItem().equals("")) {
                    pushAllDataFromDbToTable();
                } else {
                    pushChoiceDataFromDbToTable();
                }
            }
        });
    }

    private void initUi() {
        initEmptyProductsTable();
        loadCategoriesData();
        loadSuppliersData();
    }

    private void loadCategoriesData() {
        categoriesMap = new HashMap<>();
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) categoryNameComboBox.getModel();
        DefaultComboBoxModel comboBoxModel2 = (DefaultComboBoxModel) categoryNameComboBox2.getModel();
        comboBoxModel.addElement("");
        comboBoxModel2.addElement("");
        for (Categories x : categoriesService.listCategories()) {
            categoriesMap.put(x.getCategoryName(), x);
            comboBoxModel.addElement(x.getCategoryName());
            comboBoxModel2.addElement(x.getCategoryName());
        }
    }

    private void loadSuppliersData() {
        suppliersMap = new HashMap<>();
        DefaultComboBoxModel comboBoxModel = (DefaultComboBoxModel) supplierNameComboBox.getModel();
        DefaultComboBoxModel comboBoxModel2 = (DefaultComboBoxModel) supplierNameComboBox2.getModel();
        comboBoxModel.addElement("");
        comboBoxModel2.addElement("");
        for (Suppliers suppliers : suppliersService.listSuppliers()) {
            suppliersMap.put(suppliers.getCompanyName(), suppliers);
            comboBoxModel.addElement(suppliers.getCompanyName());
            comboBoxModel2.addElement(suppliers.getCompanyName());
        }
    }

    private void initEmptyProductsTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ROW_NUMBER, COLUMN_HEADINGS.length);
        model.setColumnIdentifiers(COLUMN_HEADINGS);
        productsTable.setModel(model);
        productsTable.setRowSelectionAllowed(true);
        productsTable.setCellSelectionEnabled(false);
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
        supplierNameComboBox2.setSelectedIndex(0);
        supplierNameComboBox.setSelectedIndex(0);
        categoryNameComboBox2.setSelectedIndex(0);
        categoryNameComboBox.setSelectedIndex(0);
        minPricePlaceholderTextField.setText("");
        maxPricePlaceholderTextField.setText("");
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

    private void pushChoiceDataFromDbToTable() {
        List<Products> productsList = null;

        if (minPricePlaceholderTextField.getText().isEmpty() && maxPricePlaceholderTextField.getText().isEmpty()) {
            if (supplierNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsByCategories(0, Double.MAX_VALUE, categoriesMap.get(categoryNameComboBox2.getSelectedItem()).getCategoryId());
            } else if (categoryNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsBySupplier(0, Double.MAX_VALUE, suppliersMap.get(supplierNameComboBox2.getSelectedItem()).getSupplierId());
            } else {
                productsList = productsService.listChoiceProducts(0, Double.MAX_VALUE, categoriesMap.get(categoryNameComboBox2.getSelectedItem()).getCategoryId(), suppliersMap.get(supplierNameComboBox2.getSelectedItem()).getSupplierId());
            }
        } else if (maxPricePlaceholderTextField.getText().isEmpty()) {
            if (supplierNameComboBox2.getSelectedItem().equals("") && categoryNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsByPrice(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.MAX_VALUE);
            } else if (supplierNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsByCategories(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.MAX_VALUE, categoriesMap.get(categoryNameComboBox2.getSelectedItem()).getCategoryId());
            } else if (categoryNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsBySupplier(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.MAX_VALUE, suppliersMap.get(supplierNameComboBox2.getSelectedItem()).getSupplierId());
            } else {
                productsList = productsService.listChoiceProducts(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.MAX_VALUE, categoriesMap.get(categoryNameComboBox2.getSelectedItem()).getCategoryId(), suppliersMap.get(supplierNameComboBox2.getSelectedItem()).getSupplierId());
            }
        } else if (minPricePlaceholderTextField.getText().isEmpty()) {
            if (supplierNameComboBox2.getSelectedItem().equals("") && categoryNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsByPrice(0, Double.parseDouble(maxPricePlaceholderTextField.getText()));
            } else if (supplierNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsByCategories(0, Double.parseDouble(maxPricePlaceholderTextField.getText()), categoriesMap.get(categoryNameComboBox2.getSelectedItem()).getCategoryId());
            } else if (categoryNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsBySupplier(0, Double.parseDouble(maxPricePlaceholderTextField.getText()), suppliersMap.get(supplierNameComboBox2.getSelectedItem()).getSupplierId());
            } else {
                productsList = productsService.listChoiceProducts(0, Double.parseDouble(maxPricePlaceholderTextField.getText()), categoriesMap.get(categoryNameComboBox2.getSelectedItem()).getCategoryId(), suppliersMap.get(supplierNameComboBox2.getSelectedItem()).getSupplierId());
            }
        } else {
            if (supplierNameComboBox2.getSelectedItem().equals("") && categoryNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsByPrice(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.parseDouble(maxPricePlaceholderTextField.getText()));
            } else if (supplierNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsByCategories(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.parseDouble(maxPricePlaceholderTextField.getText()), categoriesMap.get(categoryNameComboBox2.getSelectedItem()).getCategoryId());
            } else if (categoryNameComboBox2.getSelectedItem().equals("")) {
                productsList = productsService.listProductsBySupplier(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.parseDouble(maxPricePlaceholderTextField.getText()), suppliersMap.get(supplierNameComboBox2.getSelectedItem()).getSupplierId());
            } else {
                productsList = productsService.listChoiceProducts(Double.parseDouble(minPricePlaceholderTextField.getText()), Double.parseDouble(maxPricePlaceholderTextField.getText()), categoriesMap.get(categoryNameComboBox2.getSelectedItem()).getCategoryId(), suppliersMap.get(supplierNameComboBox2.getSelectedItem()).getSupplierId());
            }
        }

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
