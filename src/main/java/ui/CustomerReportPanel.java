package ui;

import helpers.ServiceHelper;
import service.CustomerReportServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

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
    private CustomerReportServiceImpl bestCustomerReportService = new ServiceHelper().getCustomerReportService();

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
        List<Object[]> resultList = bestCustomerReportService.generateReport();
        DefaultTableModel tableModel = (DefaultTableModel) bestCustomerReportTable.getModel();
        tableModel.setRowCount(0);

        for (Object[] result : resultList) {
            tableModel.addRow(result);
        }
    }

    private void initUI() {
        initEmptyBestCustomerReportTable();
    }

    private void initEmptyBestCustomerReportTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ROW_NUMBER, COLUMN_HEADINGS.length);
        model.setColumnIdentifiers(COLUMN_HEADINGS);
        bestCustomerReportTable.setModel(model);
        bestCustomerReportTable.setRowSelectionAllowed(true);
        bestCustomerReportTable.setCellSelectionEnabled(false);

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
        bestCustomerReportPanel = new JPanel();
        bestCustomerReportPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(3, 1, new Insets(0, 0, 0, 0), -1, -1));
        tablePanel = new JPanel();
        tablePanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        bestCustomerReportPanel.add(tablePanel, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        tablePanel.setBorder(BorderFactory.createTitledBorder("Best Customer Report"));
        final JScrollPane scrollPane1 = new JScrollPane();
        tablePanel.add(scrollPane1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_WANT_GROW, null, null, null, 0, false));
        bestCustomerReportTable = new JTable();
        scrollPane1.setViewportView(bestCustomerReportTable);
        descriptionPanel = new JPanel();
        descriptionPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 1, new Insets(0, 0, 0, 0), -1, -1));
        bestCustomerReportPanel.add(descriptionPanel, new com.intellij.uiDesigner.core.GridConstraints(1, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        descriptionPanel.setBorder(BorderFactory.createTitledBorder("Description"));
        final JLabel label1 = new JLabel();
        label1.setText("Display customer report for each month");
        descriptionPanel.add(label1, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_WEST, com.intellij.uiDesigner.core.GridConstraints.FILL_NONE, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        buttonPanel = new JPanel();
        buttonPanel.setLayout(new com.intellij.uiDesigner.core.GridLayoutManager(1, 2, new Insets(0, 0, 0, 0), -1, -1));
        bestCustomerReportPanel.add(buttonPanel, new com.intellij.uiDesigner.core.GridConstraints(2, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_BOTH, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, null, null, null, 0, false));
        buttonPanel.setBorder(BorderFactory.createTitledBorder("Actions"));
        generateReportButton = new JButton();
        generateReportButton.setText("Generate report");
        buttonPanel.add(generateReportButton, new com.intellij.uiDesigner.core.GridConstraints(0, 0, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
        clearButton = new JButton();
        clearButton.setText("Clear");
        buttonPanel.add(clearButton, new com.intellij.uiDesigner.core.GridConstraints(0, 1, 1, 1, com.intellij.uiDesigner.core.GridConstraints.ANCHOR_CENTER, com.intellij.uiDesigner.core.GridConstraints.FILL_HORIZONTAL, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_SHRINK | com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_CAN_GROW, com.intellij.uiDesigner.core.GridConstraints.SIZEPOLICY_FIXED, null, null, null, 0, false));
    }

    /**
     * @noinspection ALL
     */
    public JComponent $$$getRootComponent$$$() {
        return bestCustomerReportPanel;
    }
}
