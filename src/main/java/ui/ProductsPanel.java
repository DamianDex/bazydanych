package ui;

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


    {
// GUI initializer generated by IntelliJ IDEA GUI Designer
// >>> IMPORTANT!! <<<
// DO NOT EDIT OR ADD ANY CODE HERE!
        $$$setupUI$$$();
    }

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        productsPanel = new JPanel();
        productsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        productsPanel.setBorder(BorderFactory.createTitledBorder(""));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 3, new Insets(0, 0, 0, 0), -1, -1));
        productsPanel.add(buttonPanel, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        addNewButton = new JButton();
        addNewButton.setText("Add New");
        buttonPanel.add(addNewButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Update");
        buttonPanel.add(updateButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Delete");
        buttonPanel.add(deleteButton, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentSelectionPanel = new JPanel();
        currentSelectionPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(5, 4, new Insets(0, 0, 0, 0), -1, -1));
        productsPanel.add(currentSelectionPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        currentSelectionPanel.setBorder(BorderFactory.createTitledBorder("Current Selection"));
        unitPriceTextField = new JTextField();
        unitPriceTextField.setText("");
        currentSelectionPanel.add(unitPriceTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Unit Price");
        currentSelectionPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Units On Order");
        currentSelectionPanel.add(label2, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Quantity Per Unit");
        currentSelectionPanel.add(label3, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, -1), null, 0, false));
        quantityTextField = new JTextField();
        currentSelectionPanel.add(quantityTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        discontinuedTextField = new JTextField();
        currentSelectionPanel.add(discontinuedTextField, new com.intellij.uiDesigner.core.GridConstraints(3, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        productNameTextField = new JTextField();
        currentSelectionPanel.add(productNameTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        unitsInStockTextField = new JTextField();
        currentSelectionPanel.add(unitsInStockTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        unitsOnOrderTextField = new JTextField();
        currentSelectionPanel.add(unitsOnOrderTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Product Name");
        currentSelectionPanel.add(label4, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("Units In Stock");
        currentSelectionPanel.add(label5, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Reorder Level");
        currentSelectionPanel.add(label6, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Category Name");
        currentSelectionPanel.add(label7, new com.intellij.uiDesigner.core.GridConstraints(3, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Supplier Name");
        currentSelectionPanel.add(label8, new com.intellij.uiDesigner.core.GridConstraints(4, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Discontinued");
        currentSelectionPanel.add(label9, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        reorderLevelTextField = new JTextField();
        currentSelectionPanel.add(reorderLevelTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        supplierNameComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel1 = new DefaultComboBoxModel();
        supplierNameComboBox.setModel(defaultComboBoxModel1);
        currentSelectionPanel.add(supplierNameComboBox, new com.intellij.uiDesigner.core.GridConstraints(4, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        categoryNameComboBox = new JComboBox();
        final DefaultComboBoxModel defaultComboBoxModel2 = new DefaultComboBoxModel();
        categoryNameComboBox.setModel(defaultComboBoxModel2);
        currentSelectionPanel.add(categoryNameComboBox, new com.intellij.uiDesigner.core.GridConstraints(3, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        productsDetailsPanel = new JPanel();
        productsDetailsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        productsPanel.add(productsDetailsPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        productsDetailsPanel.setBorder(BorderFactory.createTitledBorder("Products Details"));
        final JScrollPane scrollPane1 = new JScrollPane();
        productsDetailsPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        productsTable = new JTable();
        scrollPane1.setViewportView(productsTable);
        searchPanel = new JPanel();
        searchPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 5, new Insets(0, 0, 0, 0), -1, -1));
        productsPanel.add(searchPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Search"));
        categoryNameComboBox2 = new JComboBox();
        categoryNameComboBox2.setToolTipText("Category Name");
        searchPanel.add(categoryNameComboBox2, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        supplierNameComboBox2 = new JComboBox();
        supplierNameComboBox2.setToolTipText("Supplier Name");
        searchPanel.add(supplierNameComboBox2, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Supplier Name");
        searchPanel.add(label10, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Categories Name");
        searchPanel.add(label11, new com.intellij.uiDesigner.core.GridConstraints(0, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label12 = new JLabel();
        label12.setText("Min Price");
        searchPanel.add(label12, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label13 = new JLabel();
        label13.setText("Max Price");
        searchPanel.add(label13, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        minPricePlaceholderTextField = new PlaceholderTextField();
        minPricePlaceholderTextField.setPlaceholder("");
        minPricePlaceholderTextField.setToolTipText("Min Price");
        searchPanel.add(minPricePlaceholderTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        maxPricePlaceholderTextField = new PlaceholderTextField();
        maxPricePlaceholderTextField.setPlaceholder("");
        maxPricePlaceholderTextField.setToolTipText("Max Price");
        searchPanel.add(maxPricePlaceholderTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        searchButton = new JButton();
        searchButton.setText("Search");
        searchPanel.add(searchButton, new com.intellij.uiDesigner.core.GridConstraints(0, 4, 2, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return productsPanel;
    }
}
