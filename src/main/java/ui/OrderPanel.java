package ui;

import entities.Customers;
import helpers.ServiceHelper;
import service.CustomersServiceImpl;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private CustomersServiceImpl customersService = new ServiceHelper().getCustomersServiceImpl();

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
    }

    private void addActionListeners(){
        addActionListenerToCustomersIdComboBox();
        addActionListenerToNewCustomerRadioButton();
        addActionListenerToExisitingCustomerRadioButton();
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
}
