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

}
