package ui;

import entities.Customers;
import helpers.ServiceHelper;
import service.CustomersServiceImpl;
import service.ProductsServiceImpl;
import ui.custom.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableColumn;
import javax.swing.table.TableModel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class OrderPanel extends JPanel{
    private JComboBox customersIdComboBox;
    private JPanel CustomersDetailsPanel;
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
    private JTable table1;
    private JTable table2;

    private CustomersServiceImpl customersService = new ServiceHelper().getCustomersServiceImpl();
    private ProductsServiceImpl productsService = new ServiceHelper().getProductsService();

    private Customers selectedCustomer;
    private ButtonGroup radioCustomerGroup = new ButtonGroup();

    public OrderPanel(){
        initUI();
        addActionListeners();
    }

    private void initUI(){
        pushCustomersIdsFromDb();
        radioCustomerGroup.add(newCustomerRadioButton);
        radioCustomerGroup.add(existingCustomerRadioButton);
        ((ProductsAbstractTableModel) table1.getModel()).setProductsList(productsService.listProducts());
    }

    private void addActionListeners(){
        addActionListenerToCustomersIdComboBox();
        addActionListenerToNewCustomerRadioButton();
        addActionListenerToExisitingCustomerRadioButton();
        addActionListenerToProductsTableButton();
        addActionListenerToProductsFilterTextField();
    }

    private void addActionListenerToNewCustomerRadioButton(){
        newCustomerRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                clearTextFields();
                customersIdComboBox.setEnabled(false);
            }
        });
    }

    private void addActionListenerToExisitingCustomerRadioButton(){
        existingCustomerRadioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                if (selectedCustomer != null )
                    setTextFields(selectedCustomer);
                customersIdComboBox.setEnabled(true);
            }
        });
    }

    private void addActionListenerToCustomersIdComboBox(){
        customersIdComboBox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
            selectedCustomer = new Customers();
            if (!customersIdComboBox.getSelectedItem().equals("")) {
                selectedCustomer = customersService.getCustomerById((String) customersIdComboBox.getSelectedItem());
                setTextFields(selectedCustomer);
            } else
                clearTextFields();
            }
        });
    }

    //TODO add action to jTable button
    private void addActionListenerToProductsTableButton(){
        TableModel model = (ProductsAbstractTableModel) table1.getModel();
        model.addTableModelListener(new TableModelListener() {
            @Override
            public void tableChanged(TableModelEvent e) {
                if (e instanceof ProductsButtonTableModelEvent){
                    System.out.println(((ProductsButtonTableModelEvent) e).getProduct().toString());
                }
            }
        });
    }

    private void addActionListenerToProductsFilterTextField(){
        productsFilterTextField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                ((ProductsAbstractTableModel) table1.getModel())
                        .setProductsList(productsService.listProductsByName(productsFilterTextField.getText().toLowerCase()));
            }
        });
    }

    private void clearTextFields(){
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

    private void setTextFields(Customers customers){
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

    private void pushCustomersIdsFromDb(){
        List<Customers> customersList = customersService.listCustomers();
        DefaultComboBoxModel comboModel = (DefaultComboBoxModel) customersIdComboBox.getModel();
        comboModel.addElement(new String(""));
        for (Customers customers: customersList){
            comboModel.addElement(customers.getCustomerid());
        }
    }

    private void createUIComponents() {
        table1 = new JTable(new ProductsAbstractTableModel());
        TableColumn actionCol = table1.getColumnModel().getColumn(6);
        actionCol.setCellRenderer(new ProductsButtonTableRenderer());
        actionCol.setCellEditor(new ProductsButtonTableEditor());
    }
}
