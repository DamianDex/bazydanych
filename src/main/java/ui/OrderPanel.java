package ui;

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
        $$$setupUI$$$();
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

    /**
     * Method generated by IntelliJ IDEA GUI Designer
     * >>> IMPORTANT!! <<<
     * DO NOT edit this method OR call it in your code!
     *
     * @noinspection ALL
     */
    private void $$$setupUI$$$() {
        createUIComponents();
        orderPanel = new JPanel();
        orderPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(4, 1, new Insets(0, 0, 0, 0), -1, -1));
        OrderDetailsPanel = new JPanel();
        OrderDetailsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 4, new Insets(0, 0, 0, 0), -1, -1));
        orderPanel.add(OrderDetailsPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        OrderDetailsPanel.setBorder(BorderFactory.createTitledBorder("Order details"));
        final JScrollPane scrollPane1 = new JScrollPane();
        OrderDetailsPanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 4, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane1.setViewportView(ordersTable);
        totalPriceTextField = new JTextField();
        totalPriceTextField.setDisabledTextColor(new Color(-16741370));
        totalPriceTextField.setEditable(false);
        totalPriceTextField.setEnabled(false);
        totalPriceTextField.setFont(new Font("Arial", Font.BOLD, 12));
        totalPriceTextField.setForeground(new Color(-16741370));
        totalPriceTextField.setHorizontalAlignment(4);
        OrderDetailsPanel.add(totalPriceTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(80, -1), null, 0, false));
        final com.intellij.uiDesigner.core.Spacer spacer1 = new com.intellij.uiDesigner.core.Spacer();
        OrderDetailsPanel.add(spacer1, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, 1, null, null, null, 0, false));
        final JLabel label1 = new JLabel();
        label1.setText("Total Price");
        OrderDetailsPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        ProductsPanel = new JPanel();
        ProductsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(2, 2, new Insets(0, 0, 0, 0), -1, -1));
        orderPanel.add(ProductsPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        ProductsPanel.setBorder(BorderFactory.createTitledBorder("Products"));
        productsFilterTextField = new PlaceholderTextField();
        productsFilterTextField.setPlaceholder("Search product...");
        productsFilterTextField.setText("");
        ProductsPanel.add(productsFilterTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JScrollPane scrollPane2 = new JScrollPane();
        ProductsPanel.add(scrollPane2, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        scrollPane2.setViewportView(productsTable);
        loadProductsButton = new JButton();
        loadProductsButton.setText("Load All");
        ProductsPanel.add(loadProductsButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        orderButton = new JButton();
        orderButton.setLabel("Make an order");
        orderButton.setText("Make an order");
        orderPanel.add(orderButton, new com.intellij.uiDesigner.core.GridConstraints(3, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        CustomersDetailsPanel = new JPanel();
        CustomersDetailsPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 5, new Insets(0, 0, 0, 0), -1, 1));
        orderPanel.add(CustomersDetailsPanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_NORTH, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, new Dimension(581, 8), new Dimension(-1, 151), 0, false));
        CustomersDetailsPanel.setBorder(BorderFactory.createTitledBorder("Customers details"));
        newCustomerRadioButton = new JRadioButton();
        newCustomerRadioButton.setSelected(true);
        newCustomerRadioButton.setText("New Customer");
        CustomersDetailsPanel.add(newCustomerRadioButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        existingCustomerRadioButton = new JRadioButton();
        existingCustomerRadioButton.setText("Existing Customer");
        CustomersDetailsPanel.add(existingCustomerRadioButton, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        customersIdComboBox = new JComboBox();
        customersIdComboBox.setEnabled(false);
        CustomersDetailsPanel.add(customersIdComboBox, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        companyNameTextField = new PlaceholderTextField();
        companyNameTextField.setPlaceholder("Company Name");
        companyNameTextField.setRequestFocusEnabled(true);
        companyNameTextField.setToolTipText("Company Name");
        CustomersDetailsPanel.add(companyNameTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        contactNameTextField = new PlaceholderTextField();
        contactNameTextField.setPlaceholder("Contact Name");
        contactNameTextField.setToolTipText("Contact Name");
        CustomersDetailsPanel.add(contactNameTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        contactTitleTextField = new PlaceholderTextField();
        contactTitleTextField.setPlaceholder("Contact Title");
        contactTitleTextField.setText("");
        contactTitleTextField.setToolTipText("Contact Title");
        CustomersDetailsPanel.add(contactTitleTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        regionTextField = new PlaceholderTextField();
        regionTextField.setPlaceholder("Region");
        regionTextField.setToolTipText("Region");
        CustomersDetailsPanel.add(regionTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        cityTextField = new PlaceholderTextField();
        cityTextField.setPlaceholder("City");
        cityTextField.setToolTipText("City");
        CustomersDetailsPanel.add(cityTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 2, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        countryTextField = new PlaceholderTextField();
        countryTextField.setPlaceholder("Country");
        countryTextField.setToolTipText("Country");
        CustomersDetailsPanel.add(countryTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        postalCodeTextField = new PlaceholderTextField();
        postalCodeTextField.setPlaceholder("Postal Code");
        postalCodeTextField.setToolTipText("Postal Code");
        CustomersDetailsPanel.add(postalCodeTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 3, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        faxTextField = new PlaceholderTextField();
        faxTextField.setPlaceholder("Fax");
        faxTextField.setToolTipText("Fax");
        CustomersDetailsPanel.add(faxTextField, new com.intellij.uiDesigner.core.GridConstraints(2, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        phoneTextField = new PlaceholderTextField();
        phoneTextField.setPlaceholder("Phone");
        phoneTextField.setToolTipText("Phone");
        CustomersDetailsPanel.add(phoneTextField, new com.intellij.uiDesigner.core.GridConstraints(1, 4, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
        addressTextField = new PlaceholderTextField();
        addressTextField.setPlaceholder("Address");
        addressTextField.setToolTipText("Address");
        CustomersDetailsPanel.add(addressTextField, new com.intellij.uiDesigner.core.GridConstraints(0, 3, 1, 2, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(100, 24), null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return orderPanel;
    }
}
