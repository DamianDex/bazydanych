package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import entities.Customers;
import helpers.ServiceHelper;
import service.CustomerReportServiceImpl;
import service.CustomersServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerReportPanel extends JPanel {
    private static final int INITIAL_ROW_NUMBER = 0;
    private static final String[] COLUMN_HEADINGS = {"Year", "Month", "Customer Name", "Sales value"};
    private JPanel bestCustomerReportPanel;
    private JPanel tablePanel;
    private JPanel descriptionPanel;
    private JPanel buttonPanel;
    private JButton generateReportButton;
    private JButton clearButton;
    private JTable bestCustomerReportTable;
    private JComboBox companyNameComboBox;
    private JTextField companyNameTextField;

    private ServiceHelper serviceHelper = new ServiceHelper();
    private CustomerReportServiceImpl bestCustomerReportService = new ServiceHelper().getCustomerReportService();
    private CustomersServiceImpl customersService = serviceHelper.getCustomersServiceImpl();

    private Map<String, Customers> customersMap;

    public CustomerReportPanel() {
        initUI();
        addAllListeners();
    }

    private void addAllListeners() {
        addActionListenerToGenerateReportButton();
        addActionListenerToClearButton();
    }

    private void addActionListenerToClearButton() {
        clearButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                initEmptyBestCustomerReportTable();
            }
        });
    }

    private void addActionListenerToGenerateReportButton() {
        generateReportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                pushDataFromDbToTable();
            }
        });
    }

    private void pushDataFromDbToTable() {
        List<Object[]> resultList = bestCustomerReportService.generateCustomerReport(customersMap.get(companyNameComboBox.getSelectedItem()).getCompanyname());
        DefaultTableModel tableModel = (DefaultTableModel) bestCustomerReportTable.getModel();
        tableModel.setRowCount(0);

        for (Object[] result : resultList) {
            tableModel.addRow(result);
        }
    }

    private void initUI() {
        initEmptyBestCustomerReportTable();
        loadCustomersData();
    }

    private void initEmptyBestCustomerReportTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ROW_NUMBER, COLUMN_HEADINGS.length);
        model.setColumnIdentifiers(COLUMN_HEADINGS);
        bestCustomerReportTable.setModel(model);
        bestCustomerReportTable.setRowSelectionAllowed(true);
        bestCustomerReportTable.setCellSelectionEnabled(false);

    }
    private void loadCustomersData() {
        customersMap = new HashMap<>();
        DefaultComboBoxModel comboModel = (DefaultComboBoxModel) companyNameComboBox.getModel();
        comboModel.addElement("");
        for (Customers c : customersService.listCustomers()) {
            customersMap.put(c.getCompanyname(), c);
            comboModel.addElement(c.getCompanyname());
        }
    }

}
