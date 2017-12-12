package ui;

import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import helpers.ServiceHelper;
import service.SalesReportServiceImpl;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class SalesReportPanel extends JPanel {
    private JPanel reportsPanel;
    private JTable salesReportTable;
    private JButton generateBtn;
    private JPanel buttonPanel;
    private JPanel tablePanel;
    private JButton clearBtn;

    private static final int INITIAL_ROW_NUMBER = 0;
    private static final String[] COLUMN_HEADINGS = {"Year", "Month", "Sales value"};

    private SalesReportServiceImpl salesReportService = new ServiceHelper().getSalesReportService();

    public SalesReportPanel() {
        initUI();
        addAllActionListeners();
    }

    private void addAllActionListeners() {
        addActionListenerToGenerateBtn();
        addActionListenerToClearBtn();
    }

    private void addActionListenerToGenerateBtn() {
        generateBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                pushDataFromDbToTable();
            }
        });
    }

    private void addActionListenerToClearBtn() {
        clearBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                initEmptySalesReportTable();
            }
        });
    }

    private void pushDataFromDbToTable() {
        List<Object[]> resultsList = salesReportService.generateReport();
        DefaultTableModel tableModel = (DefaultTableModel) salesReportTable.getModel();
        tableModel.setRowCount(0); //clear JTable

        for (Object[] result : resultsList) {
            tableModel.addRow(result);
        }
    }

    private void initEmptySalesReportTable() {
        DefaultTableModel model = new DefaultTableModel(INITIAL_ROW_NUMBER, COLUMN_HEADINGS.length);
        model.setColumnIdentifiers(COLUMN_HEADINGS);
        salesReportTable.setModel(model);
        salesReportTable.setRowSelectionAllowed(true);
        salesReportTable.setCellSelectionEnabled(false);
    }

    private void initUI() {
        initEmptySalesReportTable();
    }

}
