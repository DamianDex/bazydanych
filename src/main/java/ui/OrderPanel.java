package ui;

import entities.Customers;
import helpers.ServiceHelper;
import service.CustomersServiceImpl;
import service.ProductsServiceImpl;
import ui.custom.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;
import java.awt.event.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderPanel extends JPanel {
    private JComboBox customersIdComboBox;
    private JRadioButton newCustomerRadioButton;
    private JRadioButton existingCustomerRadioButton;
    private PlaceholderTextField companyNameTextField;
    private PlaceholderTextField contactNameTextField;
    private PlaceholderTextField contactTitleTextField;
    private PlaceholderTextField regionTextField;
    private PlaceholderTextField cityTextField;
    private PlaceholderTextField addressTextField;
    private PlaceholderTextField postalCodeTextField;
    private PlaceholderTextField countryTextField;
    private PlaceholderTextField phoneTextField;
    private PlaceholderTextField faxTextField;
    private JPanel orderPanel;
    private JButton OrderButton;
    private JPanel ProductsPanel;
    private JPanel OrderDetailsPanel;
    private PlaceholderTextField productsFilterTextField;
    private JTable productsTable;
    private JTable ordersTable;
    private JTextField textField1;
    private JPanel CustomersDetailsPanel;
    private JButton loadProductsButton;

    private CustomersServiceImpl customersService = new ServiceHelper().getCustomersServiceImpl();
    private ProductsServiceImpl productsService = new ServiceHelper().getProductsService();


    private Map<String, Customers> customersMap;
    private Customers selectedCustomer;
    private ButtonGroup radioCustomerGroup = new ButtonGroup();

    private static final String[] ORDERS_COLUMN_NAMES = {"Order ID", "Product ID", "Unit Price", "Quantity", "Discount", "Total for Product"};
    private static final int INITIAL_ORDERS_ROW_NUMBER = 0;

    private ProductsAbstractTableModel productsAbstractTableModel;

    public OrderPanel() {
        initUI();
        addActionListeners();
    }

    private void initUI() {
        radioCustomerGroup.add(newCustomerRadioButton);
        radioCustomerGroup.add(existingCustomerRadioButton);
        initEmptyOrdersTable();
    }

    private void initEmptyOrdersTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ORDERS_ROW_NUMBER, ORDERS_COLUMN_NAMES.length);
        model.setColumnIdentifiers(ORDERS_COLUMN_NAMES);
        ordersTable.setModel(model);
        ordersTable.setRowSelectionAllowed(true);
        ordersTable.setCellSelectionEnabled(false);
    }

    private void addActionListeners() {
        addActionListenerToCustomersIdComboBox();
        addActionListenerToNewCustomerRadioButton();
        addActionListenerToExisitingCustomerRadioButton();
        addActionListenerToProductsTableButton();
        addActionListenerToLoadProductsButton();
        addActionListenerToProductsFilterTextField();
    }

    private void addActionListenerToNewCustomerRadioButton() {
        newCustomerRadioButton.addActionListener(actionEvent -> {
            clearTextFields();
            customersIdComboBox.setEnabled(false);
        });
    }

    private void addActionListenerToExisitingCustomerRadioButton() {
        existingCustomerRadioButton.addActionListener(actionEvent -> {
            if (customersMap == null){
                loadCustomersData();
            }
            if (selectedCustomer != null)
                setTextFields(selectedCustomer);
            customersIdComboBox.setEnabled(true);
        });
    }

    private void addActionListenerToCustomersIdComboBox() {
        customersIdComboBox.addActionListener(actionEvent -> {
            selectedCustomer = new Customers();
            if (!customersIdComboBox.getSelectedItem().equals("")) {
                selectedCustomer = customersMap.get(customersIdComboBox.getSelectedItem());
                setTextFields(selectedCustomer);
            } else
                clearTextFields();
        });
    }

    //TODO add action to jTable button
    private void addActionListenerToProductsTableButton() {
        productsAbstractTableModel.addTableModelListener(e -> {
            if (e instanceof ProductsButtonTableModelEvent) {
                System.out.println(((ProductsButtonTableModelEvent) e).getProduct().toString());
            }
        });
    }

    private void addActionListenerToProductsFilterTextField() {
        productsFilterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (!productsFilterTextField.getText().isEmpty())
                    productsAbstractTableModel.filterProductsList(productsFilterTextField.getText());
                else
                    productsAbstractTableModel.showAllProducts();
            }
        });
    }

    private void addActionListenerToLoadProductsButton(){
        loadProductsButton.addActionListener(e -> {
            if (productsAbstractTableModel.isProductsListEmpty())
                productsAbstractTableModel.setProductsList(productsService.listProducts());
            else
                productsAbstractTableModel.showAllProducts();
                productsFilterTextField.setText("");
        });
    }

    private void loadCustomersData(){
        customersMap = new HashMap<>();
        DefaultComboBoxModel comboModel = (DefaultComboBoxModel) customersIdComboBox.getModel();
        comboModel.addElement("");
        for (Customers c: customersService.listCustomers()) {
            customersMap.put(c.getCustomerid(), c);
            comboModel.addElement(c.getCustomerid());
        }
    }

    private void clearTextFields() {
        companyNameTextField.setText("");
        contactNameTextField.setText("");
        contactTitleTextField.setText("");
        regionTextField.setText("");
        cityTextField.setText("");
        addressTextField.setText("");
        postalCodeTextField.setText("");
        countryTextField.setText("");
        phoneTextField.setText("");
        faxTextField.setText("");
    }

    private void setTextFields(Customers customers) {
        companyNameTextField.setText(customers.getCompanyname());
        contactNameTextField.setText(customers.getContactname());
        contactTitleTextField.setText(customers.getContacttitle());
        regionTextField.setText(customers.getRegion());
        cityTextField.setText(customers.getCity());
        addressTextField.setText(customers.getAddress());
        postalCodeTextField.setText(customers.getPostalcode());
        countryTextField.setText(customers.getCountry());
        phoneTextField.setText(customers.getPhone());
        faxTextField.setText(customers.getFax());
    }

    private void createUIComponents() {
        productsAbstractTableModel = new ProductsAbstractTableModel();
        productsTable = new JTable(productsAbstractTableModel);
        TableColumn actionCol = productsTable.getColumnModel().getColumn(6);
        actionCol.setCellRenderer(new ProductsButtonTableRenderer());
        actionCol.setCellEditor(new ProductsButtonTableEditor());
    }
}
