package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import com.intellij.uiDesigner.core.Spacer;
import entities.Customers;
import entities.OrderDetails;
import entities.Orders;
import helpers.ServiceHelper;
import helpers.SessionHelper;
import org.hibernate.cfg.Configuration;
import service.CustomersServiceImpl;
import service.OrdersServiceImpl;
import service.ProductsServiceImpl;
import ui.custom.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.*;
import java.util.List;
import java.util.stream.IntStream;

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
    private JButton orderButton;
    private JPanel ProductsPanel;
    private JPanel OrderDetailsPanel;
    private PlaceholderTextField productsFilterTextField;
    private JTable productsTable;
    private JTable ordersTable;
    private JTextField totalPriceTextField;
    private JPanel CustomersDetailsPanel;
    private JButton loadProductsButton;

    private ServiceHelper serviceHelper = new ServiceHelper();

    private CustomersServiceImpl customersService = serviceHelper.getCustomersServiceImpl();
    private ProductsServiceImpl productsService = serviceHelper.getProductsService();
    private OrdersServiceImpl ordersService = serviceHelper.getOrdersServiceImpl();

    SessionHelper sessionHelper = new SessionHelper(new Configuration().configure().buildSessionFactory());


    private Map<String, Customers> customersMap;
    private Customers selectedCustomer;
    private ButtonGroup radioCustomerGroup = new ButtonGroup();


    private ProductsAbstractTableModel productsAbstractTableModel;
    private OrdersAbstractTableModel ordersAbstractTableModel;

    public OrderPanel() {
        initUI();
        addActionListeners();
    }

    private void initUI() {
        radioCustomerGroup.add(newCustomerRadioButton);
        radioCustomerGroup.add(existingCustomerRadioButton);
    }

    private void addActionListeners() {
        addActionListenerToCustomersIdComboBox();
        addActionListenerToNewCustomerRadioButton();
        addActionListenerToExisitingCustomerRadioButton();
        addActionListenerToProductsTableButton();
        addActionListenerToLoadProductsButton();
        addActionListenerToProductsFilterTextField();
        addActionListenerToOrdersTableButton();
        addActionListenerToMakeOrderButton();
    }

    private void addActionListenerToNewCustomerRadioButton() {
        newCustomerRadioButton.addActionListener(actionEvent -> {
            clearTextFields();
            customersIdComboBox.setEnabled(false);
        });
    }

    private void addActionListenerToExisitingCustomerRadioButton() {
        existingCustomerRadioButton.addActionListener(actionEvent -> {
            if (customersMap == null) {
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

    private void addActionListenerToProductsTableButton() {
        productsAbstractTableModel.addTableModelListener(e -> {
            if (e instanceof ProductsButtonTableModelEvent) {
                boolean hasInStock = ordersAbstractTableModel.addNewOrder(((ProductsButtonTableModelEvent) e).getProduct());
                if (!hasInStock)
                    JOptionPane.showMessageDialog(this, "You can't order more products on this type",
                            "Quantity bigger than Units In Stock", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void addActionListenerToOrdersTableButton() {
        ordersAbstractTableModel.addTableModelListener((TableModelEvent e) -> {
            if (e instanceof OrdersButtonTableModelEvent) {
                int result = JOptionPane.showConfirmDialog(this,
                        "Do you really want to delete this order?");
                if (result == 0) {
                    ordersAbstractTableModel.removeOrder(((OrdersButtonTableModelEvent) e).getRow());
                }
            } else if (e instanceof TotalPriceChangedTableModelEvent) {
                double sum = IntStream.range(0, ordersAbstractTableModel.getRowCount())
                        .mapToDouble(i -> (double) ordersTable.getValueAt(i, 4)).sum();
                sum = ((double) Math.round(sum * 100)) / 100.0d;
                totalPriceTextField.setText(String.valueOf(sum));
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

    private void addActionListenerToLoadProductsButton() {
        loadProductsButton.addActionListener(e -> {
            if (productsAbstractTableModel.isProductsListEmpty())
                productsAbstractTableModel.setProductsList(productsService.listProducts());
            else
                productsAbstractTableModel.showAllProducts();
            productsFilterTextField.setText("");
        });
    }

    private void addActionListenerToMakeOrderButton() {
        orderButton.addActionListener(e -> {
            boolean accept = true;
            if (companyNameTextField.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Customer is empty!", "Empty customer", JOptionPane.ERROR_MESSAGE);
                accept = false;
            }
            if (ordersAbstractTableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(this, "Orders are empty!", "Empty orders", JOptionPane.ERROR_MESSAGE);
                accept = false;
            }
            if (accept) {
                int result = JOptionPane.showConfirmDialog(this, getOrderSummary(), "Summary", JOptionPane.INFORMATION_MESSAGE);
                if (result == 0) {
                    try {
                        saveOrder();
                        productsAbstractTableModel.setProductsList(productsService.listProducts());
                        ordersAbstractTableModel.clearRecords();
                        JOptionPane.showMessageDialog(this, "Order made successfully.");
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, ex.getMessage());
                    }
                }
            }

        });

    }

    private JScrollPane getOrderSummary() {
        JLabel label = new JLabel();
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setText("<html><pre><font size = 14>Summary</font><br>  " +
                "<br>" + fixedLengthString("Company Name:", 15) + companyNameTextField.getText() +
                "<br>" + fixedLengthString("Address:", 15) + addressTextField.getText() +
                "<br>" + fixedLengthString("Postal Code:", 15) + postalCodeTextField.getText() +
                "<br>" + fixedLengthString("City:", 15) + cityTextField.getText() +
                "<br>" + fixedLengthString("Region:", 15) + regionTextField.getText() +
                "<br>" + fixedLengthString("Country:", 15) + countryTextField.getText() +
                "<br>" + fixedLengthString("Contact Title:", 15) + contactTitleTextField.getText() +
                "<br>" + fixedLengthString("Contact Name:", 15) + contactNameTextField.getText() +
                "<br>" + fixedLengthString("Phone:", 15) + phoneTextField.getText() +
                "<br>" + fixedLengthString("Fax:", 15) + faxTextField.getText() +
                "<br><br><font size = 14>Order Details </font>" +
                "<br>" + getOrderDetailsString() +
                "<br><br><font size = 12 color = #007F00>Total price: " + totalPriceTextField.getText() + "</font>" +
                "<br><br><font size = 12>Do you want to make this order?</font>" +
                "<br></pre></html>");
        JScrollPane scrollPane = new JScrollPane(label, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setPreferredSize(new Dimension(1000, 600));
        return scrollPane;
    }

    private String getOrderDetailsString() {
        String[] headers = ordersAbstractTableModel.getHeaders();
        String[][] rows = new String[ordersAbstractTableModel.getRowCount()][ordersAbstractTableModel.getColumnCount() - 1];
        Arrays.setAll(rows, i -> ordersAbstractTableModel.getLine(i));
        int[] columnsWidths = new int[ordersAbstractTableModel.getColumnCount() - 1];
        Arrays.fill(columnsWidths, 0);
        Arrays.stream(rows).forEach(row -> IntStream.range(0, row.length)
                .filter(i -> columnsWidths[i] < row[i].length()).forEach(i -> columnsWidths[i] = row[i].length()));
        IntStream.range(0, columnsWidths.length).filter(i -> columnsWidths[i] < 14).forEach(i -> columnsWidths[i] = 14);

        String output = new String("<html><pre>");
        for (int i = 0; i < headers.length - 1; i++) {
            output += fixedLengthString(headers[i], columnsWidths[i] + 4);
        }

        for (String[] row : rows) {
            output += "<br>";
            for (int i = 0; i < row.length; i++) {
                output += fixedLengthString(row[i], columnsWidths[i] + 4);
            }
        }
        return output;
    }

    public static String fixedLengthString(String string, int length) {
        return String.format("%-" + length + "s", string);
    }

    private void loadCustomersData() {
        customersMap = new HashMap<>();
        DefaultComboBoxModel comboModel = (DefaultComboBoxModel) customersIdComboBox.getModel();
        comboModel.addElement("");
        for (Customers c : customersService.listCustomers()) {
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

    private Customers getCustomer() {
        if (selectedCustomer != null && existingCustomerRadioButton.isSelected())
            return selectedCustomer;
        else {
            Customers customers = new Customers();
            customers.setAddress(addressTextField.getText());
            customers.setCity(cityTextField.getText());
            customers.setCompanyname(companyNameTextField.getText());
            customers.setContactname(contactNameTextField.getText());
            customers.setContacttitle(contactTitleTextField.getText());
            customers.setCountry(countryTextField.getText());
            customers.setFax(faxTextField.getText());
            customers.setPhone(phoneTextField.getText());
            customers.setPostalcode(postalCodeTextField.getText());
            customers.setRegion(regionTextField.getText());
            customers.generateCustomerId();
            return customers;
        }
    }

    private Orders getOrder() {
        Orders order = new Orders();
        order.setEmployeeId(1);
        order.setShipVia(1);
        order.setOrderDate(new Date());
        return order;
    }

    private void saveOrder() {
        Orders orders = getOrder();
        orders.setCustomers(getCustomer());
        List<OrderDetails> orderDetailsList = ordersAbstractTableModel.getOrderDetailsList();

        orderDetailsList.forEach(x -> {
            x.getPk().setOrders(orders);
        });

        Set<OrderDetails> odSet = new HashSet<>(orderDetailsList);
        orders.setOrderDetails(odSet);
        ordersService.addOrder(orders);
    }

    private void createUIComponents() {
        productsAbstractTableModel = new ProductsAbstractTableModel();
        ordersAbstractTableModel = new OrdersAbstractTableModel();
        productsTable = new JTable(productsAbstractTableModel);
        ordersTable = new JTable(ordersAbstractTableModel);
        Color green = new Color(34, 139, 34);
        TableColumn actionColProducts = productsTable.getColumnModel().getColumn(6);
        actionColProducts.setCellRenderer(new ProductsButtonTableRenderer("+", green));
        actionColProducts.setCellEditor(new ProductsButtonTableEditor("+", green));
        actionColProducts.setMaxWidth(60);
        TableColumn actionColOrders = ordersTable.getColumnModel().getColumn(5);
        actionColOrders.setCellRenderer(new ProductsButtonTableRenderer("-", Color.red));
        actionColOrders.setCellEditor(new ProductsButtonTableEditor("-", Color.red));
        actionColOrders.setMaxWidth(60);

    }

}
