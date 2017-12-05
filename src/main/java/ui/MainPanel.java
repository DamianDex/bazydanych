package ui;


import javax.swing.*;

public class MainPanel {

    private JPanel mainPanel;
    private JTabbedPane tabsPanel;
    private JPanel categoriesTab;
    private JPanel ordersTab;
    private JPanel productsTab;
    private JPanel salesReport;
    private CustomerReportPanel bestCustomerReportPanel1;
    private SupplierPanel supplierPanel1;

    public MainPanel() {

    }

    public static void main(String[] args) {
        JFrame jFrame = new JFrame("Bazy danych");
        MainPanel mainPanel = new MainPanel();
        jFrame.setContentPane(mainPanel.mainPanel);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();
        jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        jFrame.setVisible(true);

        mainPanel.initUI();
    }

    private void initUI() {
    }
}