package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import entities.Suppliers;
import helpers.ServiceHelper;
import javafx.util.converter.PercentageStringConverter;
import service.SuppliersServiceImpl;

import javax.persistence.PersistenceException;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Comparator;
import java.util.List;

public class SupplierPanel extends JPanel {

    private static final String[] COLUMN_HEADINGS = {"Supplier ID", "Company Name", "Contact Name", "Contact Title", "Address",
            "City", "Region", "Postal Code", "Country", "Phone", "Fax", "Home Page"};
    private static final String SUPPLIER_CANT_REMOVE = "Supplier still use by products";
    private static int INITIAL_ROW_NUMBER = 0;

    private JPanel supplierPanel;
    private JPanel buttonPanel;
    private JButton readAllButton;
    private JButton addNewButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JPanel currentSelectionPanel;
    private JPanel supplierDetailsPanel;
    private JTable suppliersTable;
    private JTextField companyNameTextField;
    private JTextField postalCodeTextField;
    private JTextField contactNameTextField;
    private JTextField contactTitleTextField;
    private JTextField addressTextField;
    private JTextField cityTextField;
    private JTextField countryTextField;
    private JTextField phoneTextField;
    private JTextField faxTextField;
    private JTextField homePageTextField;
    private JTextField regionTextField;

    private SuppliersServiceImpl suppliersService = new ServiceHelper().getSuppliersService();
    private Suppliers selectedSuppliers;

    public SupplierPanel() {
        initUi();
        addAllActionListeners();
    }

    private void addAllActionListeners() {
        addActionListenerToReadAllBtn();
        addActionListenerToTableRowSelection();
        addActionListenerToUpdateBtn();
        addActionListenerToAddNewBtn();
        addActionListenerToDeleteBtn();
        addMouseListenerToSupplierPanel();
    }

    private void addMouseListenerToSupplierPanel() {
        supplierPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                clearSupplierTextFields();
            }
        });
    }

    private void addActionListenerToDeleteBtn() {
        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    suppliersService.deletesupplier(selectedSuppliers.getSupplierId());
                } catch (PersistenceException exception) {
                    showErrorDialog(SUPPLIER_CANT_REMOVE);
                }
                pushDataFromDbToTable();
            }
        });
    }

    private void showErrorDialog(String errorMessage) {
        JOptionPane.showMessageDialog(new JFrame(), errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void addActionListenerToUpdateBtn() {
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suppliersService.updateSupplier(getSupplierFromTextFields());
                pushDataFromDbToTable();
            }
        });
    }

    private void addActionListenerToAddNewBtn() {
        addNewButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                suppliersService.addSupplier(getSupplierFromTextFields());
                pushDataFromDbToTable();
            }
        });
    }

    private void addActionListenerToTableRowSelection() {
        ListSelectionModel cellSelectionModel = suppliersTable.getSelectionModel();
        cellSelectionModel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        cellSelectionModel.addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent e) {
                try {
                    Integer id = Integer.valueOf(suppliersTable.getValueAt(suppliersTable.getSelectedRow(), 0).toString());
                    selectedSuppliers = suppliersService.getSupplierById(id);
                    setSupplierTextFields(selectedSuppliers);
                } catch (Exception ex) {
                    //do nothing
                }
            }
        });
    }

    private void addActionListenerToReadAllBtn() {
        readAllButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pushDataFromDbToTable();
            }
        });
    }

    private void initUi() {
        initEmptySuppliersTable();
    }

    private void initEmptySuppliersTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ROW_NUMBER, COLUMN_HEADINGS.length);
        model.setColumnIdentifiers(COLUMN_HEADINGS);
        suppliersTable.setModel(model);
        suppliersTable.setRowSelectionAllowed(true);
        suppliersTable.setCellSelectionEnabled(false);
    }

    private void pushDataFromDbToTable() {
        List<Suppliers> suppliersList = suppliersService.listSuppliers();
        DefaultTableModel tableModel = (DefaultTableModel) suppliersTable.getModel();
        tableModel.setRowCount(0);

        suppliersList.sort(new Comparator<Suppliers>() {
            @Override
            public int compare(Suppliers o1, Suppliers o2) {
                if (o1.getSupplierId() > o2.getSupplierId()) return 1;
                else return -1;
            }
        });

        for (Suppliers suppliers : suppliersList) {
            tableModel.addRow(suppliers.toArray());
        }
    }

    private void setSupplierTextFields(Suppliers suppliers) {
        companyNameTextField.setText(selectedSuppliers.getCompanyName());
        contactNameTextField.setText(selectedSuppliers.getContactName());
        contactTitleTextField.setText(selectedSuppliers.getContactTitle());
        addressTextField.setText(selectedSuppliers.getAddress());
        cityTextField.setText(selectedSuppliers.getCity());
        regionTextField.setText(selectedSuppliers.getRegion());
        postalCodeTextField.setText(selectedSuppliers.getPostalCode());
        countryTextField.setText(selectedSuppliers.getCountry());
        phoneTextField.setText(selectedSuppliers.getPhone());
        faxTextField.setText(selectedSuppliers.getFax());
        homePageTextField.setText(selectedSuppliers.getHomePage());
    }

    private Suppliers getSupplierFromTextFields() {
        Suppliers suppliersToAdd = new Suppliers();
        suppliersToAdd.setSupplierId(selectedSuppliers.getSupplierId());
        suppliersToAdd.setCompanyName(companyNameTextField.getText());
        suppliersToAdd.setContactName(contactNameTextField.getText());
        suppliersToAdd.setContactTitle(contactTitleTextField.getText());
        suppliersToAdd.setAddress(addressTextField.getText());
        suppliersToAdd.setRegion(regionTextField.getText());
        suppliersToAdd.setCity(cityTextField.getText());
        suppliersToAdd.setCountry(countryTextField.getText());
        suppliersToAdd.setFax(faxTextField.getText());
        suppliersToAdd.setPhone(phoneTextField.getText());
        suppliersToAdd.setPostalCode(postalCodeTextField.getText());
        suppliersToAdd.setHomePage(homePageTextField.getText());

        return suppliersToAdd;
    }

    private void clearSupplierTextFields() {
        companyNameTextField.setText("");
        contactNameTextField.setText("");
        contactTitleTextField.setText("");
        addressTextField.setText("");
        cityTextField.setText("");
        regionTextField.setText("");
        postalCodeTextField.setText("");
        countryTextField.setText("");
        phoneTextField.setText("");
        faxTextField.setText("");
        homePageTextField.setText("");
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
        supplierPanel = new JPanel();
        supplierPanel.setLayout(new GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayoutManager(1, 4, new Insets(0, 0, 0, 0), -1, -1));
        supplierPanel.add(buttonPanel, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        readAllButton = new JButton();
        readAllButton.setText("Read All");
        buttonPanel.add(readAllButton, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        addNewButton = new JButton();
        addNewButton.setText("Add New");
        buttonPanel.add(addNewButton, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        updateButton = new JButton();
        updateButton.setText("Update");
        buttonPanel.add(updateButton, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        deleteButton = new JButton();
        deleteButton.setText("Delete");
        buttonPanel.add(deleteButton, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        currentSelectionPanel = new JPanel();
        currentSelectionPanel.setLayout(new GridLayoutManager(6, 4, new Insets(0, 0, 0, 0), -1, -1));
        supplierPanel.add(currentSelectionPanel, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        currentSelectionPanel.setBorder(BorderFactory.createTitledBorder("Current Selection"));
        final JLabel label1 = new JLabel();
        label1.setText("Company Name");
        currentSelectionPanel.add(label1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label2 = new JLabel();
        label2.setText("Contact Name");
        currentSelectionPanel.add(label2, new GridConstraints(1, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label3 = new JLabel();
        label3.setText("Contact Title");
        currentSelectionPanel.add(label3, new GridConstraints(2, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label4 = new JLabel();
        label4.setText("Address");
        currentSelectionPanel.add(label4, new GridConstraints(3, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label5 = new JLabel();
        label5.setText("City");
        currentSelectionPanel.add(label5, new GridConstraints(4, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label6 = new JLabel();
        label6.setText("Region");
        currentSelectionPanel.add(label6, new GridConstraints(5, 0, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        companyNameTextField = new JTextField();
        currentSelectionPanel.add(companyNameTextField, new GridConstraints(0, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label7 = new JLabel();
        label7.setText("Postal Code");
        currentSelectionPanel.add(label7, new GridConstraints(0, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        postalCodeTextField = new JTextField();
        currentSelectionPanel.add(postalCodeTextField, new GridConstraints(0, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        contactNameTextField = new JTextField();
        currentSelectionPanel.add(contactNameTextField, new GridConstraints(1, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        contactTitleTextField = new JTextField();
        currentSelectionPanel.add(contactTitleTextField, new GridConstraints(2, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        addressTextField = new JTextField();
        currentSelectionPanel.add(addressTextField, new GridConstraints(3, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        cityTextField = new JTextField();
        currentSelectionPanel.add(cityTextField, new GridConstraints(4, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        countryTextField = new JTextField();
        currentSelectionPanel.add(countryTextField, new GridConstraints(1, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        phoneTextField = new JTextField();
        currentSelectionPanel.add(phoneTextField, new GridConstraints(2, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        faxTextField = new JTextField();
        currentSelectionPanel.add(faxTextField, new GridConstraints(3, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        homePageTextField = new JTextField();
        currentSelectionPanel.add(homePageTextField, new GridConstraints(4, 3, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        regionTextField = new JTextField();
        currentSelectionPanel.add(regionTextField, new GridConstraints(5, 1, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_HORIZONTAL, GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_FIXED, null, new Dimension(150, -1), null, 0, false));
        final JLabel label8 = new JLabel();
        label8.setText("Country");
        currentSelectionPanel.add(label8, new GridConstraints(1, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label9 = new JLabel();
        label9.setText("Phone");
        currentSelectionPanel.add(label9, new GridConstraints(2, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label10 = new JLabel();
        label10.setText("Fax");
        currentSelectionPanel.add(label10, new GridConstraints(3, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        final JLabel label11 = new JLabel();
        label11.setText("Home Page");
        currentSelectionPanel.add(label11, new GridConstraints(4, 2, 1, 1, GridConstraints.ANCHOR_WEST, GridConstraints.FILL_NONE, GridConstraints.SIZEPOLICY_FIXED, GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        supplierDetailsPanel = new JPanel();
        supplierDetailsPanel.setLayout(new GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        supplierPanel.add(supplierDetailsPanel, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        supplierDetailsPanel.setBorder(BorderFactory.createTitledBorder("Supplier Details"));
        final JScrollPane scrollPane1 = new JScrollPane();
        supplierDetailsPanel.add(scrollPane1, new GridConstraints(0, 0, 1, 1, GridConstraints.ANCHOR_CENTER, GridConstraints.FILL_BOTH, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, GridConstraints.SIZEPOLICY_CAN_SHRINK | GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        suppliersTable = new JTable();
        scrollPane1.setViewportView(suppliersTable);
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return supplierPanel;
    }
}
